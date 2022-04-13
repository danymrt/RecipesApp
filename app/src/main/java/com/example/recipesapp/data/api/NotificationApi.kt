package com.example.recipesapp.data.api

import com.example.recipesapp.R
import com.example.recipesapp.data.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationApi {

    @Headers("Authorization: key="+ R.string.cloud_messaging, "Content-Type:application/json")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification) : Response<ResponseBody>

}