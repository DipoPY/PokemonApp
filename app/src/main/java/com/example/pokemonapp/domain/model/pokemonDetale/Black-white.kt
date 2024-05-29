package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class Black_white (

  @SerializedName("animated"           ) var animated         : Animated? = Animated(),
  @SerializedName("back_default"       ) var backDefault      : String?   = null,
  @SerializedName("back_female"        ) var backFemale       : String?   = null,
  @SerializedName("back_shiny"         ) var backShiny        : String?   = null,
  @SerializedName("back_shiny_female"  ) var backShinyFemale  : String?   = null,
  @SerializedName("front_default"      ) var frontDefault     : String?   = null,
  @SerializedName("front_female"       ) var frontFemale      : String?   = null,
  @SerializedName("front_shiny"        ) var frontShiny       : String?   = null,

)