package com.example.pokemonapp.presentation.ui.screens

import PokemonsUiState
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pokemonapp.data.model.Pokemon
import isInternetConnected

@Composable
fun HomeScreen(
    context: Context,
    pokemonsUiState: PokemonsUiState,
    retryAction: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit
) {
    val isConnected = isInternetConnected(context)

    if (!isConnected && pokemonsUiState !is PokemonsUiState.Success) {
        ErrorScreen(
            retryAction = retryAction,
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    when (pokemonsUiState) {
        is PokemonsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is PokemonsUiState.Success -> PokemonsScreen(
            pokemons = pokemonsUiState.pokemonSearch,
            onPokemonClick = onPokemonClick,
            retryAction = retryAction
        )
        is PokemonsUiState.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = Modifier.fillMaxSize()
        )

        else -> {}
    }
}