package com.example.apps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.apps.databinding.ActivityMain3Binding
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

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    private var name: String? = null
    private var username: String? = null
    private lateinit var pokemon: Pokemon
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.extras?.getString("name")
        username = intent.extras?.getString("username")

        Toast.makeText(this, "Hola $name", Toast.LENGTH_LONG).show()

        getPoke()

        binding.catch2Button.setOnClickListener(::catchPokemon)
    }

    private fun getPoke() {
        Log.e("zzzzzzzzzzzz", "Entro")
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

                if (name != null && life != null && attack != null && defense != null && speed != null && image != null) {
                    showPokemon(name, life, attack, defense, speed, image)
                    Log.e("Name",    name)
                    Log.e("Life",    life)
                    Log.e("Attack",    attack)
                    Log.e("Defense",    defense)
                }

            }


        }
    }

    private fun showPokemon(name: String, life: String, attack: String, defense: String, speed: String, image: String){

        binding.namePokemonText.text = name
        binding.defenseText.text = defense
        binding.attackText.text = attack
        binding.lifeText.text = life
        binding.speedText.text = speed
        Glide.with(this).load(image).into(binding.imageView)

        createPokemon(name, life, attack, defense, speed, image)


    }

    private fun createPokemon(name: String, life: String?, attack: String?, defense: String?, speed: String?, image: String) {

        username?.let {

            pokemon = Pokemon(
                UUID.randomUUID().toString(),
                name,
                image,
                defense.toString(),
                attack.toString(),
                speed.toString(),
                Date().time,
                life.toString(),
                username.toString()


            )
        }

    }

    private fun catchPokemon(view: View){

        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("users").document(pokemon.username).collection("pokemon").document(pokemon.name).set(pokemon)
        }

    }
}