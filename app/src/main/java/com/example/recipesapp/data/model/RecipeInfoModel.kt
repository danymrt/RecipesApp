package com.example.recipesapp.data.model


import com.google.gson.annotations.SerializedName


data class RecipeInfoModel (

    @SerializedName("id"                       ) var id                       : Int?                           = null,
    @SerializedName("title"                    ) var title                    : String?                        = null,
    @SerializedName("image"                    ) var image                    : String?                        = null,
    @SerializedName("imageType"                ) var imageType                : String?                        = null,
    @SerializedName("servings"                 ) var servings                 : Int?                           = null,
    @SerializedName("readyInMinutes"           ) var readyInMinutes           : Int?                           = null,
    @SerializedName("license"                  ) var license                  : String?                        = null,
    @SerializedName("sourceName"               ) var sourceName               : String?                        = null,
    @SerializedName("sourceUrl"                ) var sourceUrl                : String?                        = null,
    @SerializedName("spoonacularSourceUrl"     ) var spoonacularSourceUrl     : String?                        = null,
    @SerializedName("aggregateLikes"           ) var aggregateLikes           : Int?                           = null,
    @SerializedName("healthScore"              ) var healthScore              : Int?                           = null,
    @SerializedName("spoonacularScore"         ) var spoonacularScore         : Int?                           = null,
    @SerializedName("pricePerServing"          ) var pricePerServing          : Double?                        = null,
    @SerializedName("analyzedInstructions"     ) var analyzedInstructions     : ArrayList<AnalyzedInstructions>              = arrayListOf(),
    @SerializedName("cheap"                    ) var cheap                    : Boolean?                       = null,
    @SerializedName("creditsText"              ) var creditsText              : String?                        = null,
    @SerializedName("cuisines"                 ) var cuisines                 : ArrayList<String>              = arrayListOf(),
    @SerializedName("dairyFree"                ) var dairyFree                : Boolean?                       = null,
    @SerializedName("diets"                    ) var diets                    : ArrayList<String>              = arrayListOf(),
    @SerializedName("gaps"                     ) var gaps                     : String?                        = null,
    @SerializedName("glutenFree"               ) var glutenFree               : Boolean?                       = null,
    @SerializedName("instructions"             ) var instructions             : String?                        = null,
    @SerializedName("ketogenic"                ) var ketogenic                : Boolean?                       = null,
    @SerializedName("lowFodmap"                ) var lowFodmap                : Boolean?                       = null,
    @SerializedName("occasions"                ) var occasions                : ArrayList<String>              = arrayListOf(),
    @SerializedName("sustainable"              ) var sustainable              : Boolean?                       = null,
    @SerializedName("vegan"                    ) var vegan                    : Boolean?                       = null,
    @SerializedName("vegetarian"               ) var vegetarian               : Boolean?                       = null,
    @SerializedName("veryHealthy"              ) var veryHealthy              : Boolean?                       = null,
    @SerializedName("veryPopular"              ) var veryPopular              : Boolean?                       = null,
    @SerializedName("whole30"                  ) var whole30                  : Boolean?                       = null,
    @SerializedName("weightWatcherSmartPoints" ) var weightWatcherSmartPoints : Int?                           = null,
    @SerializedName("dishTypes"                ) var dishTypes                : ArrayList<String>              = arrayListOf(),
    @SerializedName("extendedIngredients"      ) var extendedIngredients      : ArrayList<ExtendedIngredients> = arrayListOf(),
    @SerializedName("summary"                  ) var summary                  : String?                        = null,
    @SerializedName("winePairing"              ) var winePairing              : WinePairing?                   = WinePairing()

)

data class ExtendedIngredients (
    @SerializedName("aisle"        ) var aisle        : String?           = null,
    @SerializedName("amount"       ) var amount       : Any?              = null,
    @SerializedName("consitency"   ) var consitency   : String?           = null,
    @SerializedName("id"           ) var id           : Int?              = null,
    @SerializedName("image"        ) var image        : String?           = null,
    @SerializedName("measures"     ) var measures     : Measures?         = Measures(),
    @SerializedName("meta"         ) var meta         : ArrayList<String> = arrayListOf(),
    @SerializedName("name"         ) var name         : String?           = null,
    @SerializedName("original"     ) var original     : String?           = null,
    @SerializedName("originalName" ) var originalName : String?           = null,
    @SerializedName("unit"         ) var unit         : String?           = null
)

data class Measures (
    @SerializedName("metric" ) var metric : Metric? = Metric(),
    @SerializedName("us"     ) var us     : Us?     = Us()
)

data class Metric (

    @SerializedName("amount"    ) var amount    : Any?    = null,
    @SerializedName("unitLong"  ) var unitLong  : String? = null,
    @SerializedName("unitShort" ) var unitShort : String? = null
)

data class Us (
    @SerializedName("amount"    ) var amount    : Any?    = null,
    @SerializedName("unitLong"  ) var unitLong  : String? = null,
    @SerializedName("unitShort" ) var unitShort : String? = null
)

data class WinePairing (
    @SerializedName("pairedWines"    ) var pairedWines    : ArrayList<String>         = arrayListOf(),
    @SerializedName("pairingText"    ) var pairingText    : String?                   = null,
    @SerializedName("productMatches" ) var productMatches : ArrayList<ProductMatches> = arrayListOf()
)

data class ProductMatches (
    @SerializedName("id"            ) var id            : Int?    = null,
    @SerializedName("title"         ) var title         : String? = null,
    @SerializedName("description"   ) var description   : String? = null,
    @SerializedName("price"         ) var price         : String? = null,
    @SerializedName("imageUrl"      ) var imageUrl      : String? = null,
    @SerializedName("averageRating" ) var averageRating : Double? = null,
    @SerializedName("ratingCount"   ) var ratingCount   : Int?    = null,
    @SerializedName("score"         ) var score         : Double? = null,
    @SerializedName("link"          ) var link          : String? = null
)

data class AnalyzedInstructions (
    @SerializedName("name"  ) var name  : String?          = null,
    @SerializedName("steps" ) var steps : ArrayList<Steps> = arrayListOf()
)