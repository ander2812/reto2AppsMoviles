package com.example.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter : RecyclerView.Adapter<PokeDexView>() {

    private val pokemonList = ArrayList<Pokemon>()

    fun addPokemon(pokemon: Pokemon){
        pokemonList.add(pokemon)
        notifyItemInserted(pokemonList.size-1)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeDexView {

        var inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.activity_poke_dex_row, parent, false)
        val pokeDexView = PokeDexView(row)
        return pokeDexView

    }

    override fun onBindViewHolder(skeleton: PokeDexView, position: Int) {
        val chart = pokemonList[position]
        skeleton.pokemon = chart
        skeleton.initializer(chart)

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun deleteAllPokemon(){
        pokemonList.clear()
    }
}