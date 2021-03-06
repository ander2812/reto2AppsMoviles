package com.example.apps

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class PokeDexView(itemView:View) : RecyclerView.ViewHolder(itemView) {

    var pokemon: Pokemon? = null

    private var nameRow : TextView = itemView.findViewById(R.id.nameText)
    private var dateRow : TextView = itemView.findViewById(R.id.dateText)
    private var imageRow : ImageView = itemView.findViewById(R.id.pImage)

    @SuppressLint("SimpleDateFormat")
    fun initializer(pokemonView: Pokemon){
        Glide.with(imageRow.context).load(pokemon?.image).into(imageRow)
        nameRow.text = pokemonView.name
        dateRow.text = SimpleDateFormat("MMM dd, yy 'at' HH:mm").format(Date(pokemonView.date))

        pokemonDetails()
    }

    private fun pokemonDetails(){
        imageRow.setOnClickListener {
            val intent = Intent(itemView.context, MainActivity4::class.java)
            intent.putExtra("name", pokemon!!.name)
            intent.putExtra("life", pokemon!!.life)
            intent.putExtra("speed", pokemon!!.speed)
            intent.putExtra("defense", pokemon!!.defense)
            intent.putExtra("attack", pokemon!!.attack)
            intent.putExtra("image", pokemon!!.image)
            intent.putExtra("username", pokemon!!.username)
            intent.putExtra("id", pokemon!!.id)
            intent.putExtra("date", dateRow.text.toString())

            itemView.context.startActivity(intent)
        }

    }
}