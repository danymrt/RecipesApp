package com.example.recipesapp.data.model

import java.util.ArrayList

data class PushNotification(
    val data: NotificationData,
    val registration_ids: ArrayList<String>
)
