package com.example.pokemonapp.domain.retrofit

import com.example.pokemonapp.domain.repository.PokemonsRepository

interface AppContainer {
    val pokemonRepository: PokemonsRepository
}
