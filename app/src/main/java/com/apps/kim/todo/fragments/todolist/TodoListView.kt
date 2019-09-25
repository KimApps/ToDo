package com.apps.kim.todo.fragments.todolist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent

/**
Created by KIM on 25.09.2019
 **/

interface TodoListView {
    fun cancelAlarm(pendingId: Int)
    fun getAlarmManager(): AlarmManager?
    fun getPendingIntent(id: Int, intent: Intent): PendingIntent
    fun getServiceIntent(hour: Int, minute: Int, period: Int, pendingId: Long): Intent
    fun showAlertDialog(medicineName: String)
}