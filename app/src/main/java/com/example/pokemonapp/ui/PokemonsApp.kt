package com.example.pokemonapp.ui

import PokemonsViewModel
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonapp.PokemonsApplication
import com.example.pokemonapp.ui.screens.HomeScreen
import com.example.pokemonapp.ui.screens.PokemonDetailScreen
import errorNotification
import isInternetConnected

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PokemonsApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val pokemonsViewModel: PokemonsViewModel = viewModel(
        factory = PokemonsViewModel.provideFactory(
            pokemonRepository = (context.applicationContext as PokemonsApplication).container.pokemonRepository,
            context = context
        )
    )

    Scaffold(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        context = context,
                        pokemonsUiState = pokemonsViewModel.pokemonUiState,
                        retryAction = {
                            if (isInternetConnected(context)) {
                                pokemonsViewModel.loadPokemonsFromApi(0, 50)
                            } else {
                                errorNotification(context)
                            }
                        },
                        onPokemonClick = { pokemon ->
                            navController.navigate("detail/${pokemon.name}")
                        }
                    )
                }
                composable("detail/{pokemonName}") { backStackEntry ->
                    val pokemonName = backStackEntry.arguments?.getString("pokemonName")
                    val pokemon = pokemonsViewModel.getPokemonByName(pokemonName!!)
                    if (pokemon != null) {
                        PokemonDetailScreen(
                            pokemon = pokemon,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}


