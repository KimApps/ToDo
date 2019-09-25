package com.apps.kim.todo.alarm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
Created by KIM on 25.09.2019
 **/

class RebootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, RebootService::class.java)
        context?.startService(serviceIntent)
    }
}