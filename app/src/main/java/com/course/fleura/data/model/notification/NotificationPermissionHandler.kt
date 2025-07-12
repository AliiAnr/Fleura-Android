// 3. Create a NotificationPermissionHandler to request permissions:
package com.course.fleura.data.model.notification

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.course.fleura.MainActivity

object NotificationPermissionHandler {
    const val NOTIFICATION_PERMISSION_CODE = 100

    private fun areNotificationsEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For devices below Android 13, permission is granted at install time
            true
        }
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!areNotificationsEnabled(activity)) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }
}

object DownloadPermissionHandler {
    const val WRITE_STORAGE_PERMISSION_CODE = 101

    // Store pending download info for MainActivity to access
    private var pendingDownloadUrl: String? = null
    private var pendingDownloadFilename: String? = null

    // Check if WRITE_EXTERNAL_STORAGE permission is granted (only API ≤28)
    private fun hasWritePermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 10+ doesn't need the permission for Downloads
        }

    fun requestDownload(context: Context, url: String, filename: String) {
        // Store the info regardless - it will be needed if permission is granted
        pendingDownloadUrl = url
        pendingDownloadFilename = filename

        // If context is MainActivity, also set the pending download there
        (context as? MainActivity)?.setPendingDownload(url, filename)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (hasWritePermission(context)) {
                startDownload(context, url, filename)
            } else {
                if (context is android.app.Activity) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_STORAGE_PERMISSION_CODE
                    )
                } else {
                    Toast.makeText(context, "Cannot request permission: context is not an Activity", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Android 10+ doesn't need explicit permission
            startDownload(context, url, filename)
        }
    }

    fun startDownload(context: Context, url: String, filename: String) {
        try {
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(url.toUri())
                .setTitle("QR Code")
                .setDescription("Downloading QR code")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            dm.enqueue(request)
            Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun getPendingDownloadInfo(): Pair<String?, String?> {
        return Pair(pendingDownloadUrl, pendingDownloadFilename)
    }

    fun clearPendingDownloadInfo() {
        pendingDownloadUrl = null
        pendingDownloadFilename = null
    }
}