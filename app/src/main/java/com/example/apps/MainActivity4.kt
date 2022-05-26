package com.example.apps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.apps.databinding.ActivityMain3Binding
import com.example.apps.databinding.ActivityMain4Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityMain4Binding
    private var name: String? = null
    private var life: String? = null
    private var defense: String? = null
    private var attack: String? = null
    private var date: String? = null
    private var username: String? = null
    private var id: String? = null
    private var image: String? = null
    private lateinit var pokemon: Pokemon
    private var speed: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.extras?.getString("name")
        defense = intent.extras?.getString("defense")
        life = intent.extras?.getString("life")
        attack = intent.extras?.getString("attack")
        speed = intent.extras?.getString("speed")
        date = intent.extras?.getString("date")
        username = intent.extras?.getString("username")
        id = intent.extras?.getString("id")
        image = intent.extras?.getString("image")

        binding.deleteButton.setOnClickListener(::deletePokemon)


        showPokemon(name, defense, life, attack, speed, image)
    }

    private fun showPokemon(name: String?, life: String?, attack: String?, defense: String?, speed: String?, image: String?){

        binding.namePokemonText2.text = name
        binding.defenseText2.text = defense
        binding.attackText2.text = attack
        binding.lifeText2.text = life
        binding.speedText2.text = speed
        Glide.with(this).load(image).into(binding.imageView2)


    }

    private fun deletePokemon(view: View){
        pokemon = Pokemon(id!!, name!!, image!!, defense!!, attack!!, speed!!, date!!.toLong(), life!!, username!!)
        Firebase.firestore.collection("users").document(pokemon.username).collection("pokemon").document(
            pokemon.id).delete()
    }
}