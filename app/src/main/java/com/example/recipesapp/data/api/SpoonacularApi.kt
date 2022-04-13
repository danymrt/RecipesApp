package com.example.recipesapp.data.api

import com.example.recipesapp.data.model.RecipeCaloriesModel
import com.example.recipesapp.data.model.RecipeInfoModel
import com.example.recipesapp.data.model.RecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/{id}/analyzedInstructions")
    suspend fun getDetails(
        @Path("id") id: String?,
        @Query("stepBreakdown") keyword: Boolean?,
        @Query("apiKey") apiKey: String
    ): Response<List<RecipeModel?>?>

    @GET("recipes/{id}/information")
    suspend fun getInfoRecipe(
        @Path("id") id: String?,
        @Query("includeNutrition") keyword: Boolean?,
        @Query("apiKey") apiKey: String
    ): Response<RecipeInfoModel?>?

    @GET("recipes/{id}/nutritionWidget.json")
    suspend fun getInfoCalories(
        @Path("id") id: String?,
        @Query("apiKey") apiKey: String
    ): Response<RecipeCaloriesModel?>?
}