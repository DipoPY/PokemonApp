import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.domain.repository.PokemonsRepository
import com.example.pokemonapp.data.model.Pokemon
import kotlinx.coroutines.launch

sealed interface PokemonsUiState {
    data class Success(val pokemonSearch: List<Pokemon>) : PokemonsUiState
    object Error : PokemonsUiState
    object Loading : PokemonsUiState
}

class PokemonsViewModel(
    private val pokemonRepository: PokemonsRepository,
    private val context: Context
) : ViewModel() {

    var pokemonUiState: PokemonsUiState by mutableStateOf(PokemonsUiState.Loading)
        private set

    private var isCompleteLoading = false
    var searchText: String by mutableStateOf("")
    private var allPokemons: MutableList<Pokemon> = mutableListOf()

    private fun updateUiState(pokemons: List<Pokemon>) {
        pokemonUiState = PokemonsUiState.Success(pokemons)
    }

    fun filterPokemons(query: String): List<Pokemon> {
        return if (query.isEmpty()) allPokemons
        else allPokemons.filter { it.name?.contains(query, ignoreCase = true)!! }
    }

    fun onSearchQueryChanged(query: String) {
        searchText = query
        updateUiState(filterPokemons(query))
    }

    init {
        Log.d("Myy", "Инициализация ViewModel")
        viewModelScope.launch {
            val cachedPokemons = pokemonRepository.getCachedPokemons()
            if (cachedPokemons.isNotEmpty()) {
                allPokemons = cachedPokemons.toMutableList()

                updateUiState(filterPokemons(searchText))
            }
            if (isInternetConnected(context)) {
                if (cachedPokemons.size < 1302) {
                    loadPokemonsFromApi(0, 50)

                }
            } else if (cachedPokemons.isEmpty()) {
                pokemonUiState = PokemonsUiState.Error
            }
        }
    }

    fun loadPokemonsFromApi(offset: Int, limit: Int) {
        Log.d("Myy", "Загрузка покемонов с API $offset-$limit")
        if (isCompleteLoading) return

        viewModelScope.launch {
            if (pokemonUiState !is PokemonsUiState.Success) {
                pokemonUiState = PokemonsUiState.Loading
            }
            try {
                if ((isInternetConnected(context))) {
                    val pokemons = pokemonRepository.getPokemons(offset, limit)
                    if (pokemons.isNotEmpty()) {
                        val newPokemons = pokemons.filter { newPokemon ->
                            allPokemons.none { existingPokemon -> existingPokemon.url == newPokemon.url }
                        }
                        if (newPokemons.isNotEmpty()) {
                            allPokemons.addAll(newPokemons)
                            allPokemons = allPokemons.distinctBy { it.url }
                                .toMutableList() // Удаление дубликатов
                            updateUiState(filterPokemons(searchText))
                        }
                    }

                    if (allPokemons.size >= 1302 || pokemons.isEmpty()) {
                        isCompleteLoading = true
                        cachePokemonsInDatabase(allPokemons)
                    } else {
                        if (isInternetConnected(context))
                            cachePokemonsInDatabase(allPokemons)
                        loadPokemonsFromApi(offset + 50, limit + 50)
                    }
                }

            } catch (e: Exception) {
                val cachedPokemons = pokemonRepository.getCachedPokemons()
                if (cachedPokemons.isNotEmpty()) {
                    allPokemons = cachedPokemons.toMutableList()
                    updateUiState(filterPokemons(searchText))
                } else {
                    pokemonUiState = PokemonsUiState.Error
                }
            }
        }
    }

    private fun cachePokemonsInDatabase(pokemons: List<Pokemon>) {
        viewModelScope.launch {
            pokemonRepository.cachePokemons(pokemons)
        }
    }

    fun getPokemonByName(name: String): Pokemon? {
        return when (val state = pokemonUiState) {
            is PokemonsUiState.Success -> state.pokemonSearch.firstOrNull { it.name == name }
            else -> null
        }
    }

    companion object {
        fun provideFactory(
            pokemonRepository: PokemonsRepository,
            context: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(PokemonsViewModel::class.java)) {
                    return PokemonsViewModel(pokemonRepository, context) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
