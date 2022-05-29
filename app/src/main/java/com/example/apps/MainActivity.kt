package com.example.apps

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apps.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener(::login)
    }

    private fun login(view: View){
        val user = User(UUID.randomUUID().toString(), binding.userText.text.toString(), Date().time)
        Gson().toJson(user)

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