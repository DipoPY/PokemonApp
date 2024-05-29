package com.example.pokemonapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.pokemonapp.R
import com.example.pokemonapp.common.CustomFontFamily
import com.example.pokemonapp.ui.screens.help.ProgressBar
import com.example.pokemonapp.ui.theme.Pink40
import com.example.pokemonapp.ui.theme.Pink80

@Composable
fun PokemonDetailScreen(pokemon: com.example.pokemonapp.domain.model.Pokemon, onBack: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(ScrollState(0))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.image)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(Pink80)
                .border(width = 10.dp, color = Pink40, shape = RoundedCornerShape(30.dp))

        )
        pokemon.name?.let {
            val name = it.first().uppercase() + it.removeRange(0..0)
            Text(
                text = name,
                fontFamily = CustomFontFamily,
                fontSize = 30.sp,
                modifier = Modifier

                    .padding(10.dp, top = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center


            )
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(
                    id = R.drawable.ic_hp
                ),
                contentDescription = "hp",
                modifier = Modifier
                    .size(60.dp)
                    .padding(start = 5.dp),
            )
            ProgressBar(currentPoints = pokemon.statHp, maxPoints = 100, "hp")
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(
                    id = R.drawable.ic_attak
                ),
                contentDescription = "attak",
                modifier = Modifier
                    .size(60.dp)
                    .padding(start = 5.dp),
            )
            ProgressBar(currentPoints = pokemon.statAttack, maxPoints = 100, "attak")
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(
                    id = R.drawable.ic_defense
                ),
                contentDescription = "defence",
                modifier = Modifier
                    .size(60.dp)
                    .padding(start = 5.dp),
            )
            ProgressBar(currentPoints = pokemon.statDefense, maxPoints = 100, "defence")
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink40,
                contentColor = Color.Black,
            )
        ) {
            Text(
                "Back",
                fontFamily = CustomFontFamily,
                fontSize = 24.sp
            )
        }
    }
}
