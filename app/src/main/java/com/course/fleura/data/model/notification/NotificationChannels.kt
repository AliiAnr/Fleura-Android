package com.course.fleura.data.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    // ID dan info setiap channel
    const val GENERAL_CHANNEL_ID = "fleura_general_channel"
    const val GENERAL_CHANNEL_NAME = "General Notifications"
    const val GENERAL_CHANNEL_DESC = "Notifikasi umum dari Fleura"

    const val PROMO_CHANNEL_ID = "fleura_promo_channel"
    const val PROMO_CHANNEL_NAME = "Promo & Penawaran"
    const val PROMO_CHANNEL_DESC = "Promo terbaru dan penawaran spesial"

    const val ORDER_CHANNEL_ID = "fleura_order_channel"
    const val ORDER_CHANNEL_NAME = "Pesanan & Transaksi"
    const val ORDER_CHANNEL_DESC = "Update status pesanan dan transaksi"
}

object NotificationChannelUtil {
    fun createAllNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    NotificationChannels.GENERAL_CHANNEL_ID,
                    NotificationChannels.GENERAL_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = NotificationChannels.GENERAL_CHANNEL_DESC },

                NotificationChannel(
                    NotificationChannels.PROMO_CHANNEL_ID,
                    NotificationChannels.PROMO_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply { description = NotificationChannels.PROMO_CHANNEL_DESC },

                NotificationChannel(
                    NotificationChannels.ORDER_CHANNEL_ID,
                    NotificationChannels.ORDER_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = NotificationChannels.ORDER_CHANNEL_DESC }
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
}