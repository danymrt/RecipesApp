package com.example.recipesapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.ui.main.SearchActivity
import com.example.recipesapp.ui.recipe.RecipeFragment
import com.example.recipesapp.ui.recipe.tabRecipe.Comment.CommentFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

private const val CHANNEL_ID = "comment_channel"
private const val BASE_URL = "https://danymrt.pythonanywhere.com"

/**
 * FirebaseService for receiving the notification
 */

class FirebaseService : FirebaseMessagingService() {

    companion object{
        var NOTIFICATION_ID = false
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.modifyToken(MainActivity.applicationContextLogin()?.getString("id","")!!,newToken)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("FirebaseService", response.message())
                } else {
                    Log.d("FirebaseService", response.message())
                }
            }
        }
    }

    lateinit var pendingIntent : PendingIntent

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("id_recipe", message.data["id_recipe"])
        NOTIFICATION_ID = true
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannal(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,0, intent, FLAG_MUTABLE)
        } else {
            pendingIntent = PendingIntent.getActivity(this,0, intent, FLAG_ONE_SHOT)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_food_bank_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannal(notificationManager: NotificationManager){
        val channelName = "CommentChannel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "This channel is reserved for comments in the recipes"
            enableLights(true)
            lightColor = Color.GREEN
        }

        notificationManager.createNotificationChannel(channel)
    }

}