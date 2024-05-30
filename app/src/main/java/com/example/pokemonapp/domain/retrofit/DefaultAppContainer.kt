package com.example.pokemonapp.domain.retrofit

import android.content.Context
import androidx.room.Room
import com.example.pokemonapp.domain.database.PokemonDatabase
import com.example.pokemonapp.domain.repository.NetworkPokemonsRepository
import com.example.pokemonapp.domain.repository.PokemonsRepository
import com.example.pokemonapp.domain.service.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "https://pokeapi.co/api/v2/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: PokemonService by lazy {
        retrofit.create(PokemonService::class.java)
    }

    private val database: PokemonDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    override val pokemonRepository: PokemonsRepository by lazy {
        NetworkPokemonsRepository(retrofitService, database.pokemonDao())
    }
}
