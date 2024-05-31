package com.example.pokemonapp.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.pokemonapp.R
import com.example.pokemonapp.data.model.Pokemon
import com.example.pokemonapp.ui.screens.help.CustomFontFamily
import com.example.pokemonapp.presentation.ui.theme.Pink80
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PokemonsScreen(
    pokemons: List<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
    retryAction: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val filteredPokemons = pokemons.filter {
        it.name?.contains(searchQuery, ignoreCase = true)!!
    }

    if (filteredPokemons.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Nothing was found", fontFamily = CustomFontFamily, fontSize = 40.sp)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = lazyGridState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            keyboardController?.hide()
                        }
                    }
                    false
                }
        ) {
            itemsIndexed(filteredPokemons) { _, pokemon ->
                PokemonsCard(
                    pokemon = pokemon,
                    onPokemonClick = onPokemonClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if (isSearchVisible) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            SearchBarForMainScreen(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    isSearchVisible = false
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.TopCenter)
                    .focusRequester(focusRequester)
            )
        }
        IconButton(
            onClick = { isSearchVisible = !isSearchVisible },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    lazyGridState.scrollToItem(0)
                }
                retryAction()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun PokemonsCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    onPokemonClick: (Pokemon) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onPokemonClick(pokemon) }
            .padding(4.dp)
            .fillMaxWidth()
            .requiredHeight(296.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Pink80),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            GlideImage(
                imageModel = { pokemon.image },
                modifier = Modifier.size(150.dp),
                loading = {
                    Box(modifier = Modifier.matchParentSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                failure = {
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(bitmap = ImageBitmap.imageResource(R.drawable.pokemon_ball_removebg_preview), contentDescription = "pokemon")
                    }
                }
            )

            pokemon.name?.let {
                val name = it.first().uppercase() + it.removeRange(0..0)
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp, top = 18.dp),
                    fontSize = 27.sp,
                    fontFamily = CustomFontFamily
                )
            }
        }
    }
}

@Composable
fun SearchBarForMainScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
                fontFamily = CustomFontFamily,
                fontSize = 24.sp,
                color = Color.Black
            ),
            placeholder = {
                Text(
                    text = "Search",
                    fontFamily = CustomFontFamily,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}