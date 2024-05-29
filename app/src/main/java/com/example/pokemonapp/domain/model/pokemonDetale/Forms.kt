package com.example.infoPokemon

import com.google.gson.annotations.SerializedName


data class Forms (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("url"  ) var url  : String? = null

)