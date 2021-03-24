package com.example.alarmnotification.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.alarmnotification.MainActivity
import com.example.alarmnotification.R
import com.example.alarmnotification.receiver.SnoozeReceiver


private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val notificationChannel = NotificationChannel(
            context.getString(R.string.my_notification_channel_id),
            context.getString(R.string.my_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH)
            .apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = context.getString(R.string.my_notification_channel_description)
                setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + context.packageName + "/" + R.raw.sound1),
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build())
            }
        createNotificationChannel(notificationChannel)
    }

    val contentIntent = Intent(context, MainActivity::class.java)
    val snoozeIntent = Intent(context, SnoozeReceiver::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context, REQUEST_CODE, snoozeIntent, FLAGS
    )

    val alarmImage = BitmapFactory.decodeResource(context.resources, R.drawable.big_image)

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(alarmImage)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(context, context.getString(R.string.my_notification_channel_id))
        .setSmallIcon(R.drawable.ic_clock)
        .setContentTitle(context
            .getString(R.string.notification_title))
        .setContentText(context.getString(R.string.content_text))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.alarm_icn))

        .addAction(
            R.drawable.ic_snooze,
            context.getString(R.string.snooze),
            snoozePendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}