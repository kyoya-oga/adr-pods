package com.kyoya.adrpods.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.kyoya.adrpods.R

class ConnectedDeviceService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "ConnectedDeviceServiceChannel"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        // TODO: Add logic to stop the service when the device is disconnected.

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Connected Device Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        // TODO: Replace with a more descriptive notification.
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AirPods Connected")
            .setContentText("Your AirPods are connected and managed by ADR-Pods.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
