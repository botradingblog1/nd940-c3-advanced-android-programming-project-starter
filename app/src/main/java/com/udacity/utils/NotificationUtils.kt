package com.udacity.utils


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
fun NotificationManager.sendNotification(messageBody: String, fileName: String, success: Boolean, applicationContext: Context) {
    // Create the content intent for the notification, which launches this activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("file",fileName)
    contentIntent.putExtra("status",messageBody)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Set big icon
    var iconFile = if (success) R.drawable.icon_success else R.drawable.icon_failed

    var icon = BitmapFactory.decodeResource(
        applicationContext.resources,
        iconFile
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(icon)
        .bigLargeIcon(null)


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

    // Add icon to notification
    .setStyle(bigPicStyle)
    .setLargeIcon(icon)

    // Create notification
    val notification = builder.build()

    // Notify
    notify(NOTIFICATION_ID, notification)
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
