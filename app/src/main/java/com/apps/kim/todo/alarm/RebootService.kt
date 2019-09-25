package com.apps.kim.todo.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.utils.PrefProvider
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class RebootService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminderBox = App.boxStore.boxFor(TodoDB::class.java)
        val reminders = reminderBox.all
        for (reminder in reminders) if (reminder?.isPause == false) startAlarm(reminder)
        return START_STICKY
    }

    private fun startAlarm(reminder: TodoDB?) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(DATA_PENDING_ID, reminder?.id)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = reminder?.startDate ?: EMPTY_LONG
        calendar.timeInMillis += getPeriod(reminder) * EVERY_DAY
        val pendingIntent =
            PendingIntent.getBroadcast(
                this, reminder?.id?.toInt()
                    ?: 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
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

    private fun getPeriod(reminder: TodoDB?): Int {
        val days: Int
        if (reminder?.isSpecificDays == true) {
            days = nextDay(
                reminder.daySunday,
                reminder.dayMonday,
                reminder.dayTuesday,
                reminder.dayWednesday,
                reminder.dayThursday,
                reminder.dayFriday,
                reminder.daySaturday
            )
        } else {
            var startDate = reminder?.startDate ?: EMPTY_LONG
            val currentDay = Calendar.getInstance().timeInMillis
            while (currentDay > startDate) startDate += (reminder?.interval
                ?: 1) * EVERY_DAY
            days =  ((startDate - currentDay) / EVERY_DAY).toInt()
        }
        return days
    }

    private fun nextDay(vararg weekDay: Int?): Int {
        val days = arrayListOf<Int>()
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        for (day in weekDay) {
            if (day != null) {
                if (currentDay <= day) days.add(day - currentDay)
                else days.add(day + 7 - currentDay)
            }
        }
        days.sort()
        return days[0]
    }
}