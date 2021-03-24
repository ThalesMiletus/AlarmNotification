package com.example.alarmnotification.receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import androidx.core.app.AlarmManagerCompat

class SnoozeReceiver : BroadcastReceiver() {

    companion object {
        private const val REQUEST_CODE = 0
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val triggerTime = System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager,
            AlarmManager.RTC_WAKEUP,
            triggerTime, notifyPendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.cancelAll()
    }
}