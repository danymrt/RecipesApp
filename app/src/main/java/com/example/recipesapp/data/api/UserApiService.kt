package com.example.recipesapp.data.api

import com.example.recipesapp.data.ListItems
import com.example.recipesapp.data.ListUsers
import com.example.recipesapp.data.ShoppingItem
import com.example.recipesapp.data.Users
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface UserApiService {

    @GET("users")
    suspend fun getUsers(): Response<ListUsers>

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun addUser(@Body userData: Users): Response<List<Users>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<List<Users>>

    //Get first the User object, modify the fields and send the request
    @Headers("Content-Type: application/json")
    @PUT("users/{id}")
    suspend fun modifyUser(@Path("id") id: String, @Body userData: Users): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("users/{id}")
    suspend fun addItem(@Path("id") id: String, @Body itemData: ShoppingItem): Response<ResponseBody>

    @GET("users/{id}/shopping")
    suspend fun getShoppingbyId(@Path("id") id: String): Response<ArrayList<ShoppingItem>>

    @Headers("Content-Type: application/json")
    @PUT("users/{id}/shopping")
    suspend fun modifyListShopping(@Path("id") id: String, @Body itemData: ShoppingItem): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "users/{id}/shopping", hasBody = true)
    suspend fun deleteItem(@Path("id") id: String, @Body items: ListItems): Response<ResponseBody>
}
