package com.example.pokemonapp.data

import android.util.Log
import com.example.pokemonapp.data.local.PokemonDao
import com.example.pokemonapp.data.local.PokemonEntity
import com.example.pokemonapp.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

interface PokemonsRepository {
    suspend fun getPokemons(offsetQuery: Int, limitQuery: Int): List<Pokemon>
    suspend fun getCachedPokemons(): List<Pokemon>
    suspend fun cachePokemons(pokemons: List<Pokemon>)
}

class NetworkPokemonsRepository(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao
) : PokemonsRepository {
    private val pokemonCache = mutableMapOf<String, Pokemon>()

    override suspend fun getPokemons(offsetQuery: Int, limitQuery: Int): List<Pokemon> {
        return try {
            val chunkSize = limitQuery - offsetQuery
            val deferreds = (offsetQuery until limitQuery step chunkSize).map { offset ->
                coroutineScope {
                    async(Dispatchers.IO) {
                        try {
                            val results = pokemonService.pokemonsSearch(offset, chunkSize).results
                            results.mapNotNull { result ->
                                val url = result.url
                                if (url != null && !pokemonCache.containsKey(url)) {
                                    val details = pokemonService.getPokemonDetail(url)
                                    val hp =
                                        details.stats.find { it.stat?.name == "hp" }?.baseStat ?: 0
                                    val attack =
                                        details.stats.find { it.stat?.name == "attack" }?.baseStat
                                            ?: 0
                                    val defense =
                                        details.stats.find { it.stat?.name == "defense" }?.baseStat
                                            ?: 0
                                    Pokemon(
                                        name = result.name,
                                        url = url,
                                        image = details.sprites?.other?.official_artwork?.frontDefault,
                                        statHp = hp,
                                        statAttack = attack,
                                        statDefense = defense,
                                    ).also {
                                        pokemonCache[url] = it
                                    }
                                } else {
                                    null
                                }
                            }
                        } catch (e: Exception) {
                            emptyList<Pokemon>()
                        }
                    }
                }
            }

            val newPokemons = deferreds.awaitAll().flatten().filterNotNull()
            pokemonCache.putAll(newPokemons.filter { it.url != null }.associateBy { it.url!! })

            pokemonCache.values.toList()
        } catch (e: Exception) {
            getCachedPokemons()
        }
    }

    override suspend fun getCachedPokemons(): List<Pokemon> {
        var counter = 0
        return withContext(Dispatchers.IO) {
            pokemonDao.getAllPokemons().map {
                Log.d("Myy", "${it.name} ${counter++}")
                Pokemon(it.name, it.url, it.image, it.statHp, it.statAttack, it.statDefense)
            }
        }
    }

    override suspend fun cachePokemons(pokemons: List<Pokemon>) {
        withContext(Dispatchers.IO) {
            val entities = pokemons.mapNotNull {
                it.url?.let { url ->
                    PokemonEntity(url, it.name, it.image, it.statHp, it.statAttack, it.statDefense)
                }
            }
            if (entities.isNotEmpty()) {
                pokemonDao.insertAll(entities)
            }
        }
    }
}




