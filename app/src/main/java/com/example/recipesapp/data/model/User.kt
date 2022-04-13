package com.example.recipesapp.data

import com.google.gson.annotations.SerializedName

data class ListUsers (

    @SerializedName("Users" ) var Users : ArrayList<Users> = arrayListOf()

)

data class Users (
    @SerializedName("email"       ) var email      : String? = null,
    @SerializedName("family_name" ) var familyName : String? = null,
    @SerializedName("id"          ) var id         : String? = null,
    @SerializedName("image"       ) var image      : String? = null,
    @SerializedName("name"        ) var name       : String? = null,
    @SerializedName("username"    ) var username   : String? = null,
    @SerializedName("shopping"    ) var shopping   : ArrayList<ShoppingItem> = arrayListOf()
)

data class ShoppingItem (

    @SerializedName("image"    ) var image    : String? = null,
    @SerializedName("name"     ) var name     : String? = null

)

data class ListItems (
    @SerializedName("list" ) var list : ArrayList<String> = arrayListOf()
)


