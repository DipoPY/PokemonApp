package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class Other (

  @SerializedName("dream_world"      ) var dreamWorld       : DreamWorld?       = DreamWorld(),
  @SerializedName("home"             ) var home             : Home?             = Home(),
  @SerializedName("official-artwork" ) var official_artwork : Official_artwork? = Official_artwork(),
  @SerializedName("showdown"         ) var showdown         : Showdown?         = Showdown()

)