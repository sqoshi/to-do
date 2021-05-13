package com.app.todo.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.app.todo.MainActivity
import com.app.todo.R
import com.app.todo.data.TaskDatabase
import com.app.todo.repository.TaskRepository
import timber.log.Timber

/**
 * Responsible for working in background and ringing periodically till task expiration date.
 */
class NotifierWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notId = 101

    companion object {
        const val WORK_NAME = "com.app.todo.notifications.NotifierWorker"
    }

    /**
     * Connects with database checks all expiration dates and sends
     * Notification if it is close to expiration date.
     */
    override suspend fun doWork(): Result {
        val taskDao = TaskDatabase.getDatabase(applicationContext).taskDao()
        val repository: TaskRepository = TaskRepository(taskDao)
        try {
            Timber.d("Work request for sync is run %s", repository.readAllData.toString())
            createNotificationChannel()
            sendNotification()
            //TODO: sendNotification basing on repository date values.
        } catch (e: Exception) {
            return Result.retry()
        }

        return Result.success()
    }

    /**
     * Open communication channel.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task expires."
            val desc = "Your task expire in few minutes."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = desc
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     *  Send notification that click leads to new activity or current one.
     *  Dialog contains Information about task.
     */
    private fun sendNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val bitmap = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_baseline_delete_24
        )
        val bitmapLargeIcon = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_baseline_remove_24
        )


        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Todo-app")
            .setContentText("Task expires in few minutes.")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notId, builder.build())
        }
    }
}