package com.example.pokemonapp

import android.app.Application
import com.example.pokemonapp.data.AppContainer
import com.example.pokemonapp.data.DefaultAppContainer

class PokemonsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
