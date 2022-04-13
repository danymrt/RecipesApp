package com.example.recipesapp.data.model

import com.google.gson.annotations.SerializedName


data class RecipeCaloriesModel (
    @SerializedName("calories" ) var calories : String?         = null,
    @SerializedName("carbs"    ) var carbs    : String?         = null,
    @SerializedName("fat"      ) var fat      : String?         = null,
    @SerializedName("protein"  ) var protein  : String?         = null,
    @SerializedName("bad"      ) var bad      : ArrayList<Bad>  = arrayListOf(),
    @SerializedName("good"     ) var good     : ArrayList<Good> = arrayListOf()
)

data class Good (
    @SerializedName("amount"              ) var amount              : String?  = null,
    @SerializedName("indented"            ) var indented            : Boolean? = null,
    @SerializedName("percentOfDailyNeeds" ) var percentOfDailyNeeds : Double?  = null,
    @SerializedName("name"                ) var name                : String?  = null
)

data class Bad (
    @SerializedName("name"                ) var name                : String?  = null,
    @SerializedName("amount"              ) var amount              : String?  = null,
    @SerializedName("indented"            ) var indented            : Boolean? = null,
    @SerializedName("percentOfDailyNeeds" ) var percentOfDailyNeeds : Double?  = null
)