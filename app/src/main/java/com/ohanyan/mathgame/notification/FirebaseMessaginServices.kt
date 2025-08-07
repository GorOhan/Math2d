package com.ohanyan.mathgame.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ohanyan.mathgame.MainActivity
import com.ohanyan.mathgame.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService @Inject constructor() : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            Log.d(TAG, "Notification Message Body: ${it.body}")
            showNotification(it.title, it.body, it.clickAction)
        }

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            // Handle data payload
        }
    }

    override fun onNewToken(token: String) {

    }

    private fun showNotification(title: String?, message: String?, action: String?) {
        val channelId = "MyNotificationChannel"
        val notificationId = 123 // Use a unique ID for each notification

        val openActiveTabIntent = Intent(this, MainActivity::class.java)
        openActiveTabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        openActiveTabIntent.putExtra("action", action)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, openActiveTabIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android Oreo and above
        val channel = NotificationChannel(
            channelId,
            "My Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}