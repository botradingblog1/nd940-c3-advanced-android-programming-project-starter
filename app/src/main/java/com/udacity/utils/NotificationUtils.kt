package com.udacity.utils


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches this activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_status_notification_channel_id)
    )
    .setSmallIcon(R.drawable.download_icon_small)
    .setContentTitle(applicationContext
    .getString(R.string.notification_title))
    .setContentText(messageBody)
    .setContentIntent(contentPendingIntent)
    .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Create notification
    val notification = builder.build()

    // Notify
    notify(NOTIFICATION_ID, notification)
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
