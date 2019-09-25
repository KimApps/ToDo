package com.apps.kim.todo.fragments.todolist

import android.app.AlarmManager
import android.os.Build
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.classes.TAG_ALARM
import com.apps.kim.todo.tools.utils.PrefProvider
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class TodoListPresenter(val view: TodoListView) {
    private val todoBox = App.boxStore.boxFor(TodoDB::class.java)

    fun getReminderList(): List<String> {
        val tag = PrefProvider.mTag ?: TAG_ALARM
        val reminders = todoBox.query()
            .equal(TodoDB_.requestTag, tag).build().find()
        val list = mutableSetOf<String>()
        for (reminder in reminders) {
            list.add(reminder.title ?: EMPTY_STRING)
        }
        return list.toList().sorted()
    }

    fun stopDelete(medicineName: String, isDelete: Boolean) {
        val query =
            todoBox.query()
                .equal(TodoDB_.title, medicineName)
                .build()
        val reminderList = query.find()
        for (rem in reminderList) view.cancelAlarm(rem.id?.toInt() ?: 0)
        if (isDelete) query.remove()
        else {
            for (rem in reminderList) {
                rem.isPause = true
                todoBox.put(rem)
            }
        }
    }

    fun startReminders(medicineName: String) {
        val query =
            todoBox.query()
                .equal(TodoDB_.title, medicineName)
                .build()
        val reminderList = query.find()
        for (rem in reminderList) {
            setAlarm(rem)
            rem.isPause = false
            todoBox.put(rem)
        }
    }

    private fun setAlarm(reminder: TodoDB) {
        val alarmManager = view.getAlarmManager()
        val pendingIntent =
            view.getPendingIntent(
                reminder.id?.toInt() ?: 0,
                view.getServiceIntent(
                    reminder.timeHour ?: 18,
                    reminder.timeMinute ?: 0,
                    reminder.interval ?: 1,
                    reminder.id ?: 0
                )
            )
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getCalendar(reminder).timeInMillis,
                pendingIntent
            )
            else -> alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                getCalendar(reminder).timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getCalendar(reminder: TodoDB): Calendar {
        val currentTime = Calendar.getInstance()
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, reminder.timeHour ?: 18)
        startTime.set(Calendar.MINUTE, reminder.timeMinute ?: 0)
        startTime.set(Calendar.SECOND, 0)
        while (currentTime > startTime) startTime.add(Calendar.DATE, reminder.interval ?: 1)
        return startTime
    }
}