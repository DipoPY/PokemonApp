package com.example.pokemonapp.domain.database

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pokemonapp.domain.retrofit.DefaultAppContainer

class UpdateDataBase(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.e("UpdateDataBase", "Worker execution started")
        return try {
            val success = loadDataFromApi()
            if (success) {
                Log.d("UpdateDataBase", "Worker execution succeeded")
                Result.success()
            } else {
                Log.e("UpdateDataBase", "Worker execution failed")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("UpdateDataBase", "Worker execution error", e)
            Result.retry()
        }
    }

    private suspend fun loadDataFromApi(): Boolean {
        Log.e("UpdateDataBase", "Start updating database")
        val repository = DefaultAppContainer(applicationContext).pokemonRepository
        val pokemons = repository.getPokemons(0, 1302)
        repository.cachePokemons(pokemons)
        return repository.getCachedPokemons().size == 1302
    }
}
