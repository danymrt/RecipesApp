package com.example.recipesapp.data.api

import com.example.recipesapp.data.*
import com.example.recipesapp.data.model.Recipe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RecipeApiService {

    @GET("recipes")
    suspend fun getRecipes(): Response<ListRecipes>

    @Headers("Content-Type: application/json")
    @GET("notification/{id}")
    suspend fun getRecipesNotification(@Path("id") id: String): Response<ListRecipes>

    @Headers("Content-Type: application/json")
    @GET("recipes/user/{id}")
    suspend fun getFavouriteRecipe(@Path("id") id: String): Response<ListRecipes>

    @Headers("Content-Type: application/json")
    @POST("recipes")
    suspend fun addRecipe(@Body recipeData: Recipes): Response<ResponseBody>

    @GET("recipes/{id}")
    suspend fun getRecipesById(@Path("id") id: String): Response<List<Recipes>>

    @Headers("Content-Type: application/json")
    @PUT("recipes/{id}")
    suspend fun modifyToken(@Path("id") id: String, @Body token: String): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: String): Response<ResponseBody>
}