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
import com.apps.kim.todo.db.TodoDB_.startDate
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
        for (reminder in reminders) if (reminder?.isPause == false) calculateAlarm(reminder)
        return START_STICKY
    }

    private fun calculateAlarm(reminder: TodoDB?) {
        val currentTime = Calendar.getInstance().timeInMillis
        val calendar = reminder?.startDate ?: EMPTY_LONG
        if (currentTime < calendar) startAlarm(reminder, calendar)
        else startAlarm(reminder, getPeriod(reminder))
    }

    private fun startAlarm(reminder: TodoDB?, calendar: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(DATA_PENDING_ID, reminder?.id)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(
                this, reminder?.id?.toInt()
                    ?: 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar,
                pendingIntent
            )

            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar,
                pendingIntent
            )
        }
    }

    private fun getPeriod(reminder: TodoDB?): Long {
        val currentDay = Calendar.getInstance().timeInMillis
        var startDate = reminder?.startDate ?: EMPTY_LONG
        if (reminder?.isSpecificDays == true) {
            startDate += nextDay(
                reminder.daySunday,
                reminder.dayMonday,
                reminder.dayTuesday,
                reminder.dayWednesday,
                reminder.dayThursday,
                reminder.dayFriday,
                reminder.daySaturday
            ) * EVERY_DAY
            while (currentDay > startDate) startDate += 7 * EVERY_DAY
        } else {
            while (currentDay > startDate) startDate += (reminder?.interval
                ?: 1) * EVERY_DAY
        }
        return startDate
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