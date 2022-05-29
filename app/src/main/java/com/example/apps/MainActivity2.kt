package com.example.apps
import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.apps.databinding.ActivityMain2Binding
import com.example.apps.databinding.ActivityMainBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import edu.co.icesi.semana7kotlina.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class MainActivity2 :
    AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager;
    private var username:String? = null
    private lateinit var binding: ActivityMain2Binding
    private var name:String? = null
    private lateinit var pokemon: Pokemon
    private lateinit var adapter: Adapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.extras?.getString("username")

        Toast.makeText(this,"Hola $username", Toast.LENGTH_LONG).show()

        binding.catchButton.setOnClickListener(::catch)

        binding.viewButton.setOnClickListener(::view)

        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)

        adapter = Adapter()
        recyclerView.adapter = adapter

        binding.searchButton.setOnClickListener(::searchPokemon)

        getPokemon(username.toString())

    }

    private fun catch(view: View){
        name = binding.catchText.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val response = HTTPSWebUtilDomi().GETRequest("${Constans.API_URL}/${name.toString()}")

            val jsonObject = JSONObject(response)

            val stat = jsonObject.optJSONArray("stats")
            val life = stat?.getJSONObject(0)?.optString("base_stat")
            val attack = stat?.getJSONObject(1)?.optString("base_stat")
            val defense = stat?.getJSONObject(2)?.optString("base_stat")
            val speed = stat?.getJSONObject(5)?.optString("base_stat")
            val image = jsonObject.optJSONObject("sprites")?.optString("front_default")
            val type = jsonObject.optJSONArray("types")?.getJSONObject(0)?.optJSONObject("type")?.optString("name")
            val name = jsonObject.optJSONObject("species")?.optString("name")

            withContext(Dispatchers.Main){

                username?.let {

                    pokemon = Pokemon(
                        UUID.randomUUID().toString(),
                        name.toString(),
                        image.toString(),
                        defense.toString(),
                        attack.toString(),
                        speed.toString(),
                        Date().time,
                        life.toString(),
                        username.toString()


                    )
                }

            }

            lifecycleScope.launch(Dispatchers.IO){
                Firebase.firestore.collection("users").document(pokemon.username).collection("pokemon").document(pokemon.id).set(pokemon)
            }

            getPokemon(pokemon.username)


        }
    }

    fun getPokemon(username: String){
        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("users").document(username).collection("pokemon").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener { task ->
                adapter.deleteAllPokemon()
                for (document in task.result!!) {
                    val pokemon = document.toObject(Pokemon::class.java)
                    adapter.addPokemon(pokemon)
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun searchPokemon(view: View) {

        name = binding.searchText.text.toString()


        lifecycleScope.launch(Dispatchers.IO) {

            val query = Firebase.firestore.collection("users").document(username.toString()).collection("pokemon").whereEqualTo("name",name.toString())
            query.get().addOnCompleteListener { task->

                for( document in task.result){
                    Log.e(">>>>>>>>>>>>>>>>>>>", document.data.toString())
                }

                if (task.result?.size()!=null){

                    lateinit var pokemonSearch : Pokemon
                    adapter.deleteAllPokemon()

                    for (document in task.result!!){
                        pokemonSearch = document.toObject(Pokemon::class.java)
                        adapter.addPokemon(pokemonSearch)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }else{

                    Toast.makeText(this@MainActivity2,"El pokemon no existe", Toast.LENGTH_LONG).show()

                    adapter.deleteAllPokemon()
                }
            }

        }
    }

        private fun view(view: View) {

            val intent = Intent(this@MainActivity2, MainActivity3::class.java).apply {
                putExtra("name", binding.catchText.text.toString())
                putExtra("username", username.toString())
            }
            startActivity(intent)
            finish()


        }
}