package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class Emerald (

  @SerializedName("front_default" ) var frontDefault : String? = null,
  @SerializedName("front_shiny"   ) var frontShiny   : String? = null

)