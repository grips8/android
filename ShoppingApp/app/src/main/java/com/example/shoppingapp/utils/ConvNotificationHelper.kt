package com.example.shoppingapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.shoppingapp.R

object ConvNotificationHelper {
    private val CONV_CHANNEL_ID = "convo-notif"
    private val CONV_NOTIFICATION_ID = 2
    private val HI_NOTIFICATION_ID = 3
    val KEY_TEXT_REPLY = "key_text_reply"

    fun createChannel(context: Context) {
        val manager = NotificationManagerCompat.from(context)

        val name = "conversation-related-notifications"
        val descriptionText = "channel for conversation related notifications"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CONV_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        manager.createNotificationChannel(channel)
    }

    fun showConvNotification(context: Context) {
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .build()

        val resultIntent = Intent(context, NotificationReceiver::class.java)

        val resultPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            resultIntent,
            FLAG_MUTABLE
        )

        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_input_add,
            "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val notification = NotificationCompat.Builder(context, CONV_CHANNEL_ID).setDefaults(
            Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.account)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Sup, you doin good?")
            .setContentText("Let me know ...")
            .setAutoCancel(true)
            .addAction(replyAction)
            .build()

        NotificationManagerCompat.from(context).notify(CONV_NOTIFICATION_ID, notification)
    }

    fun showConvReplyNotification(context: Context, text: String) {
        val notification = NotificationCompat.Builder(context, CONV_CHANNEL_ID)
            .setContentTitle("Thanks for letting me know!")
            .setContentText("Your text: $text")
            .setSmallIcon(R.drawable.check)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(CONV_NOTIFICATION_ID, notification)
    }

    fun showServerHiNotification(context: Context, text: String) {
        val notification = NotificationCompat.Builder(context, CONV_CHANNEL_ID)
            .setContentTitle("Reply from server")
            .setContentText(text)
            .setSmallIcon(R.drawable.check)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(HI_NOTIFICATION_ID, notification)
    }

}