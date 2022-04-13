package com.example.recipesapp.data.api

import com.example.recipesapp.data.model.SupermarketModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleMapApi {

    @GET("place/nearbysearch/json")
    suspend fun getNearBy(
        @Query("location") location: String?,
        @Query("radius") radius: Int,
        @Query("type") type: String?,
        @Query("sensor") keyword: Boolean?,
        @Query("key") key: String?
    ): Response<SupermarketModel?>?
}