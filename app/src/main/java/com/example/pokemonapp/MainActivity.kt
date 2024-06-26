package com.example.pokemonapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pokemonapp.domain.database.UpdateDataBase
import com.example.pokemonapp.presentation.ui.PokemonsApp
import com.example.pokemonapp.presentation.ui.theme.PokemonAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                hideStatusBar()
                PokemonsApp()
            }
        }
        scheduleDatabaseUpdateWorker()
    }

    private fun scheduleDatabaseUpdateWorker() {
        val dataSyncWorkRequest = PeriodicWorkRequestBuilder<UpdateDataBase>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UpdateDataBaseWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            dataSyncWorkRequest
        )
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            @Suppress("DEPRECATION")
            actionBar?.hide()
        }
    }
}
