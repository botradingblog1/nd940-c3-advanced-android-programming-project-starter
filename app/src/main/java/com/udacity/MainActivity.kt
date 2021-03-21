package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.cancelNotifications
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

enum class SelectedDownloadType {
    TYPE_GLIDE,
    TYPE_UDACITY,
    TYPE_RETROFIT
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var downloadID: Long = 0
    private var selectedDownloadType = SelectedDownloadType.TYPE_GLIDE
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            when(selectedDownloadType) {
                SelectedDownloadType.TYPE_GLIDE -> download(GLIDE_URL)
                SelectedDownloadType.TYPE_UDACITY -> download(UDACITY_URL)
                SelectedDownloadType.TYPE_RETROFIT -> download(RETROFIT_URL)
            }
        }

        // Create notification channel
        createNotificationChannel(getString(R.string.download_status_notification_channel_id), getString(R.string.download_status_notification_channel_name))
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_glide ->
                    if (checked) {
                        selectedDownloadType = SelectedDownloadType.TYPE_GLIDE
                    }
                R.id.radio_button_loadapp ->
                    if (checked) {
                        selectedDownloadType = SelectedDownloadType.TYPE_UDACITY
                    }
                R.id.radio_button_retrofit ->
                    if (checked) {
                        selectedDownloadType = SelectedDownloadType.TYPE_RETROFIT
                    }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) {
                return;
            }

            val action = intent.action
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId != downloadID) {
                    return
                }

                // Check status
                val query = DownloadManager.Query()
                query.setFilterById(downloadID)
                val cursor = downloadManager.query(query)
                cursor?.let {
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            Log.d(TAG, "download successful")

                        } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                            Log.d(TAG, "download failed")
                        }
                    }
                    cursor.close()
                }

                // Sent notification
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.cancelNotifications()

                notificationManager.sendNotification(
                    context.getText(R.string.download_complete).toString(),
                    context
                )
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        //val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(false)
            notificationChannel.enableVibration(false)
            notificationChannel.description = "Download status"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        // URL definitions
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zit"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        // Notifications
        private const val CHANNEL_ID = "channelId"
    }

}
