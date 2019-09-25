package com.apps.kim.todo.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.app.MainActivity
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.utils.PrefProvider
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class AlarmService : Service() {

    private var hour: Int = 18
    private var minute: Int = 0
    private var period: Int = 1
    private var pendingId: Long = 0
    private var title: String = EMPTY_STRING
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("onStartCommand", "YES")
        pendingId = intent?.getLongExtra(DATA_PENDING_ID, -1) ?: -1
        val reminder = reminderBox.query()
            .equal(TodoDB_.id, pendingId)
            .build()
            .findFirst()
        val currentDate = Calendar.getInstance().time
        if (reminder != null && reminder.startDate ?: currentDate < currentDate) {
            val popupIntent = Intent(applicationContext, PopupService::class.java)
            applicationContext.startService(popupIntent)
            period = reminder.interval ?: 1
            hour = reminder.timeHour ?: 18
            minute = reminder.timeMinute ?: 0
            title = reminder.title ?: EMPTY_STRING

            if (reminder.isSpecificDays == false) setNotification(reminder)
            else if (isNotify(reminder)) setNotification(reminder)
            restartAlarm()
        }
        return Service.START_NOT_STICKY
    }

    private fun setNotification(reminder: TodoDB) {
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, getString(R.string.my_chanel),
                NotificationManager.IMPORTANCE_HIGH
            )
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.apply {
                description = "Description"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                setSound(getSoundUri(reminder), attributes)
            }
            notifyManager.createNotificationChannel(channel)
        }

        val intentMain = Intent(applicationContext, MainActivity::class.java)
        val pendingMain = PendingIntent.getActivity(this, 0, intentMain, 0)
        val notification: Notification?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(
                    "${reminder.title} - ${messageTag(
                        reminder.requestTag ?: TAG_ALARM
                    )}"
                )
                .setContentText("$hour : ${getMinute(minute)}")
                .setContentIntent(pendingMain)
                .setSmallIcon(R.drawable.time_notification)
                .setAutoCancel(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            notification = Notification.Builder(this)
                .setContentTitle(
                    "${reminder.title} - ${messageTag(
                        reminder.requestTag ?: TAG_ALARM
                    )}"
                )
                .setContentText("$hour : ${getMinute(minute)}")
                .setContentIntent(pendingMain)
                .setSmallIcon(R.drawable.time_notification)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setLights(Color.BLUE, 3000, 3000)
                //.setSound(Uri.parse("android.resource://com.apps.kim.caretimes/" + R.raw.animals))
                .setSound(getSoundUri(reminder))
                .setAutoCancel(true)
                .build()
        }
        notifyManager.notify(pendingId.toInt(), notification)
    }

    private fun getSoundUri(reminder: TodoDB): Uri {
        val notificationSound = reminder.requestTag ?: TAG_ALARM
        val uri = Settings.System.DEFAULT_NOTIFICATION_URI
        var uriString = EMPTY_STRING
        when (notificationSound) {
            TAG_ALARM -> uriString = PrefProvider.sound1
            TAG_FAMILY -> uriString = PrefProvider.sound2
            TAG_FRIENDS -> uriString = PrefProvider.sound3
            TAG_WORKOUT -> uriString = PrefProvider.sound4
            TAG_MEDICINES -> uriString = PrefProvider.sound5
            TAG_DIET -> uriString = PrefProvider.sound6
        }
        return if (uriString == EMPTY_STRING) uri
        else Uri.parse(uriString)
    }

    private fun isNotify(reminder: TodoDB): Boolean {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return when (today) {
            reminder.daySunday -> true
            reminder.dayMonday -> true
            reminder.dayTuesday -> true
            reminder.dayWednesday -> true
            reminder.dayThursday -> true
            reminder.dayFriday -> true
            reminder.daySaturday -> true
            else -> false
        }
    }

    private fun restartAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(DATA_PENDING_ID, pendingId)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, period)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                pendingId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getMinute(min: Int): String {
        return if (min < 10) "0$min"
        else min.toString()
    }

    private fun messageTag(tag: String): String {
        return when (tag) {
            TAG_FAMILY -> resources.getString(R.string.family)
            TAG_FRIENDS -> resources.getString(R.string.friends)
            TAG_WORKOUT -> resources.getString(R.string.workout)
            TAG_MEDICINES -> resources.getString(R.string.medicines)
            TAG_DIET -> resources.getString(R.string.diet)
            else -> resources.getString(R.string.alarms)
        }
    }
}