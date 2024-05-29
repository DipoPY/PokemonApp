package com.example.pokemonapp.data.local

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey @NonNull val url: String,
    val name: String?,
    val image: String?,
    val statHp: Int,
    val statAttack: Int,
    val statDefense: Int
)
