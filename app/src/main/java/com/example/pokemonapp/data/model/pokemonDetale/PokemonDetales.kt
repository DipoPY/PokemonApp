package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class PokemonDetales (

  @SerializedName("sprites"                  ) var sprites                : Sprites?               = Sprites(),
  @SerializedName("stats"                    ) var stats                  : ArrayList<Stats>       = arrayListOf(),

)