package com.example.pokemons

import com.google.gson.annotations.SerializedName


data class Results (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("url"  ) var url  : String? = null

)