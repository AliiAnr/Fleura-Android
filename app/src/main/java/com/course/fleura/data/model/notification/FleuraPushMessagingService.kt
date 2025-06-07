package com.course.fleura.data.model.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.course.fleura.MainActivity
import com.course.fleura.data.repository.NotificationRepository
import com.course.fleura.data.resource.Resource
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.course.fleura.R

class FleuraPushMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Panggil repository untuk kirim token ke backend (harus di background thread)
        CoroutineScope(Dispatchers.IO).launch {
            NotificationRepository.getInstance(Resource.appContext).saveToken(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Misal: dari backend, data["type"] = "promo" / "order" / "general"
        val notifType = remoteMessage.data["type"] ?: "general"
        val title = remoteMessage.data["title"] ?: "Fleura"
        val body = remoteMessage.data["body"] ?: "Ada notifikasi baru dari Fleura"

        // Tentukan channel berdasarkan type
        val channelId = when (notifType) {
            "promo" -> NotificationChannels.PROMO_CHANNEL_ID
            "order" -> NotificationChannels.ORDER_CHANNEL_ID
            else -> NotificationChannels.GENERAL_CHANNEL_ID
        }

        showNotification(title, body, channelId)
    }

    private fun showNotification(title: String, message: String, channelId: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_fleura_id)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        notificationManager.notify(channelId.hashCode(), notificationBuilder.build())
    }

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        // Handle data message & notifikasi
//
//        // Handle data message
//        if (remoteMessage.data.isNotEmpty()) {
//            // Process data payload (order updates, etc.)
//            val title = remoteMessage.data["title"] ?: "Fleura Update"
//            val body = remoteMessage.data["body"] ?: "You have a new update"
//            showNotification(title, body)
//        }
//
//        // Handle notification message
//        remoteMessage.notification?.let {
//            showNotification(it.title ?: "Fleura", it.body ?: "You have a new notification")
//        }
//    }
//
//    private fun showNotification(title: String, message: String) {
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create notification channel for Android O and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Fleura Notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "Receive updates from Fleura"
//                enableLights(true)
//            }
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Create intent for notification tap action
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Build notification
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_fleura_id)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//
//        // Show notification
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }
//
//    companion object {
//        private const val CHANNEL_ID = "fleura_notification_channel"
//        private const val NOTIFICATION_ID = 100
//    }

}