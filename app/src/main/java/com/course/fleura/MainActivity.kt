package com.course.fleura

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.course.fleura.data.model.notification.DownloadPermissionHandler
import com.course.fleura.data.model.notification.NotificationChannelUtil
import com.course.fleura.data.model.notification.NotificationPermissionHandler

class   MainActivity : ComponentActivity() {

    private var pendingDownloadUrl: String? = null
    private var pendingDownloadFilename: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        NotificationPermissionHandler.requestNotificationPermission(this)

        NotificationChannelUtil.createAllNotificationChannels(this)

        splashScreen.setKeepOnScreenCondition { true }
        Handler(Looper.getMainLooper()).postDelayed({
            splashScreen.setKeepOnScreenCondition { false }
        }, 2500)

        setContent {
            FleuraApp()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == NotificationPermissionHandler.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Notification permission granted")
            } else {
                Log.d("MainActivity", "Notification permission denied")
            }
        } else if (requestCode == DownloadPermissionHandler.WRITE_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If we have pending download info, proceed with download
                pendingDownloadUrl?.let { url ->
                    pendingDownloadFilename?.let { filename ->
                        DownloadPermissionHandler.startDownload(this, url, filename)
                        // Clear pending info
                        pendingDownloadUrl = null
                        pendingDownloadFilename = null
                    }
                }
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to set pending download info
    fun setPendingDownload(url: String, filename: String) {
        pendingDownloadUrl = url
        pendingDownloadFilename = filename
    }
}