package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


// This file is edited from the one provided with the EggTimer sample project.

// Notification ID.
val ARG_DOWNLOAD_ID = "download_id"
private val NOTIFICATION_ID = 0

// extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param messageBody message body.
 *
 * @param downloadId download id.
 *
 * @param applicationContext activity context.
 */
fun NotificationManager.sendNotification(
    messageBody: String,
    downloadId: Long,
    applicationContext: Context
) {
    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(ARG_DOWNLOAD_ID, downloadId)

    // create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val bigTextStyle = NotificationCompat.BigTextStyle()

    // get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.load_app_notification_channel_id)
    )

        // use the new 'load_app' notification channel

        // set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // add style to builder
        .setStyle(bigTextStyle)

        // add snooze action
        .addAction(
            android.R.drawable.stat_sys_download_done,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )

    // call notify
    notify(NOTIFICATION_ID, builder.build())

}
