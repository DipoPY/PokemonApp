package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class Gold (

  @SerializedName("back_default"      ) var backDefault      : String? = null,
  @SerializedName("back_shiny"        ) var backShiny        : String? = null,
  @SerializedName("front_default"     ) var frontDefault     : String? = null,
  @SerializedName("front_shiny"       ) var frontShiny       : String? = null,
  @SerializedName("front_transparent" ) var frontTransparent : String? = null

)