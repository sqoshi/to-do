package com.app.todo.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract


/**
 * Responsible for handling background workflow.
 */
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val NotificationService = NotificationService(context)

        NotificationService.createNotification()
    }
}