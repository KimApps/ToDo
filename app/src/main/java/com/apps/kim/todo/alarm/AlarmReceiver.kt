package com.apps.kim.todo.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.apps.kim.todo.tools.classes.DATA_PENDING_ID

/**
Created by KIM on 25.09.2019
 **/

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendId = intent?.extras?.getLong(DATA_PENDING_ID)
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra(DATA_PENDING_ID, pendId)
        context?.startService(serviceIntent)
    }
}