package com.example.shoppingapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {
            val title = remoteInput.getCharSequence(
                ConvNotificationHelper.KEY_TEXT_REPLY).toString()
            ConvNotificationHelper.showConvReplyNotification(context, title)
        }
    }
}