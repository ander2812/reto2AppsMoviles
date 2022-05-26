package com.example.apps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.apps.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import edu.co.icesi.semana7kotlina.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapter

    private val users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener(::login)
    }

    private fun login(view: View){
        val user = User(UUID.randomUUID().toString(), binding.userText.text.toString(), Date().time)
        val json = Gson().toJson(user)

        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("users").document(user.name).set(user)
            val intent = Intent(this@MainActivity, MainActivity2::class.java).apply {
                putExtra("username", user.name)
            }
            startActivity(intent)
            finish()
        }

    }
}