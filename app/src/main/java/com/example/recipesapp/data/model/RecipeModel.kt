package com.example.recipesapp.data.model

import com.google.gson.annotations.SerializedName


data class RecipeModel (

  @SerializedName("name"  ) var name  : String?          = null,
  @SerializedName("steps" ) var steps : ArrayList<Steps> = arrayListOf()

)

data class Steps (

  @SerializedName("number"      ) var number      : Int?                   = null,
  @SerializedName("step"        ) var step        : String?                = null,
  @SerializedName("ingredients" ) var ingredients : ArrayList<Ingredients> = arrayListOf(),
  @SerializedName("equipment"   ) var equipment   : ArrayList<Equipment>   = arrayListOf()

)

data class Ingredients (

  @SerializedName("id"            ) var id            : Int?    = null,
  @SerializedName("name"          ) var name          : String? = null,
  @SerializedName("localizedName" ) var localizedName : String? = null,
  @SerializedName("image"         ) var image         : String? = null

)

data class Equipment (

  @SerializedName("id"          ) var id          : Int?         = null,
  @SerializedName("image"       ) var image       : String?      = null,
  @SerializedName("name"        ) var name        : String?      = null,
  @SerializedName("localizedName" ) var localizedName : String? = null,
  @SerializedName("temperature" ) var temperature : Temperature? = Temperature()

)

data class Temperature (

  @SerializedName("number" ) var number : Int?    = null,
  @SerializedName("unit"   ) var unit   : String? = null

)

