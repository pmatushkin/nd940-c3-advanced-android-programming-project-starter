package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var downloadId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        binding.included.okButton.setOnClickListener {
            finish()
        }
        setContentView(binding.root)

        downloadId = intent?.extras?.getLong(ARG_DOWNLOAD_ID) ?: -1

        cancelAllNotifications()

        checkLastDownloadStatus()
    }

    private fun checkLastDownloadStatus() {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor: Cursor =
            downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

        if (cursor.moveToFirst()) {
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    binding.included.valueFileStatus.setText(R.string.label_success)
                }
                else -> {
                    binding.included.valueFileStatus.setText(R.string.label_failure)
                    binding.included.valueFileStatus.setTextColor(Color.RED)
                }
            }

            binding.included.valueFileName.text =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
        }

        cursor.close()
    }

    private fun cancelAllNotifications() {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()
    }

}
