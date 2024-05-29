package com.example.pokemonapp.data



import com.example.infoPokemon.PokemonDetales
import com.example.pokemons.Pokemons
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {
    @GET("pokemon")
    suspend fun pokemonsSearch(
        @Query("offset") offsetQuery: Int,
        @Query("limit") limitQuery: Int,

        ): Pokemons

    @GET
    suspend fun getPokemonDetail(@Url pokemonUrl: String): PokemonDetales


}