package com.example.ibeacon

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.altbeacon.beacon.BeaconManager

/*
 * We need to create a NotificationChannel associated with our CHANNEL_ID before sending a
 * notification.
 */
fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Beacon Channel Description"

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendBeaconTestNotification(
    context: Context,
    message: String,
    notificationId: Int
) {
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle("iBeacon")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setSmallIcon(R.drawable.ic_launcher_background)

    notify(notificationId, builder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.setupForegroundService() {
    val builder = Notification.Builder(this, "BeaconReferenceApp")
    builder.setSmallIcon(R.drawable.ic_launcher_foreground)
    builder.setContentTitle("Scanning for Beacons")
    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
    )
    builder.setContentIntent(pendingIntent);
    val channel = NotificationChannel(
        "beacon-ref-notification-id",
        "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT
    )
    channel.description = "My Notification Channel Description"
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel);
    builder.setChannelId(channel.id);
    BeaconManager
        .getInstanceForApplication(this)
        .enableForegroundServiceScanning(builder.build(), 456)
}

private const val CHANNEL_ID = "iBeaconNotificationChannel"
const val NOTIFICATION_ID_NOTIFIER = 167
const val NOTIFICATION_ID_MONITOR = 798
const val NOTIFICATION_ID_RANGING = 987
const val iBeaconLayout = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
private const val CHANNEL_NAME = "iBeacon-channel-name"

