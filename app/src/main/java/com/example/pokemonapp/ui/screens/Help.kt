package com.example.pokemonapp.ui.screens.help

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonapp.common.CustomFontFamily
import com.example.pokemonapp.ui.theme.AttakColor
import com.example.pokemonapp.ui.theme.DefColor
import com.example.pokemonapp.ui.theme.HpColor

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProgressBar(currentPoints: Int, maxPoints: Int = 100, target: String) {
    val fillRatio = currentPoints.toFloat() / maxPoints.toFloat()

    BoxWithConstraints(
        modifier = Modifier
            .padding(16.dp)
            .height(30.dp)
            .fillMaxWidth()
            .background(
                Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        val result = "$currentPoints / $maxPoints"

        Box(
            modifier = Modifier
                .fillMaxWidth(fillRatio)
                .height(30.dp)
                .background(
                    color = when (target) {
                        "hp" -> HpColor
                        "attak" -> AttakColor
                        else -> DefColor
                    }, shape = RoundedCornerShape(12.dp)
                )
        )
        Text(
            text = result,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.Center),
            fontFamily = CustomFontFamily,
            color = Color.Black,
        )
    }
}

@Composable
fun SearchBar(searchQuery: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        label = { Text("Search Pokemons") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = Color.LightGray,
            focusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
        )
    )
}
