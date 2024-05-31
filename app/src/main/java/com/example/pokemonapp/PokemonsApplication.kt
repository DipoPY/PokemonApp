package com.example.pokemonapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.pokemonapp.domain.retrofit.AppContainer
import com.example.pokemonapp.domain.retrofit.DefaultAppContainer

class PokemonsApplication : Application(), Configuration.Provider {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}
