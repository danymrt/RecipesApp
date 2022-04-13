package com.example.recipesapp.data

import com.google.gson.annotations.SerializedName

data class ListRecipes (
    @SerializedName("Recipes" )var Recipes : ArrayList<Recipes> = arrayListOf()
)

data class Recipes(

    @SerializedName("id")
    var id: String? = null,
    @SerializedName("id_recipe")
    var id_recipe: String? = null,
    @SerializedName("id_user")
    var id_user: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("img")
    var img: String? = null,
    @SerializedName("like")
    var like: String? = null,
    @SerializedName("token")
    val token: String? = null
)