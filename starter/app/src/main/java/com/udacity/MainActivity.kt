package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var urlString = ""
    private var urlDescription = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.completeLoading()

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        if (urlString.isEmpty()) {
            // Stop animations if no download option was selected
            custom_button.completeLoading()

            Toast.makeText(this, getString(R.string.error_please_select_option), Toast.LENGTH_SHORT)
                .show()
        } else {
            // Begin animations
            custom_button.beginLoading()

            val request =
                DownloadManager.Request(Uri.parse(urlString))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    fun onOptionSelected(view: View) {
        if (view is RadioButton) {
            when (view.id) {
                R.id.radio_glide ->
                    if (view.isChecked) {
                        urlString = getString(R.string.url_glide)
                        urlDescription = getString(R.string.label_glide)
                    }
                R.id.radio_load_app ->
                    if (view.isChecked) {
                        urlString = getString(R.string.url_load_app)
                        urlDescription = getString(R.string.label_load_app)
                    }

                R.id.radio_retrofit ->
                    if (view.isChecked) {
                        urlString = getString(R.string.url_retrofit)
                        urlDescription = getString(R.string.label_retrofit)
                    }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

}
