package com.apps.kim.todo.fragments.add

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences

/**
Created by KIM on 25.09.2019
 **/

interface AddView {
    fun getTitle(): String
    fun getStartDate(): String
    fun setSpinnerPosition(position: Int)
    fun getSpinnerPosition(): Int
    fun setSpinnerIntervalPosition(position: Int)
    fun getSpinnerIntervalPosition(): Int
    fun clearLiveData()
    fun clearTitle()
    fun startAnim()
    fun getTextButton1(): String
    fun getTextButton2(): String
    fun getTextButton3(): String
    fun getTextButton4(): String
    fun getTextButton5(): String
    fun getTextButton6(): String
    fun showButton2()
    fun showButton3()
    fun showButton4()
    fun showButton5()
    fun showButton6()
    fun hideButton2()
    fun hideButton3()
    fun hideButton4()
    fun hideButton5()
    fun hideButton6()
    fun getAlarmManager(): AlarmManager?
    fun getPendingIntent(id: Int, intent: Intent): PendingIntent
    fun getServiceIntent(pendingId: Int): Intent
    fun setSpecificDays(isActive: Boolean)
    fun setSunday(isActive: Boolean)
    fun setMonday(isActive: Boolean)
    fun setTuesday(isActive: Boolean)
    fun setWednesday(isActive: Boolean)
    fun setThursday(isActive: Boolean)
    fun setFriday(isActive: Boolean)
    fun setSaturday(isActive: Boolean)
    fun isSpecificDays(): Boolean
    fun getSunday(): Int?
    fun getMonday(): Int?
    fun getTuesday(): Int?
    fun getWednesday(): Int?
    fun getThursday(): Int?
    fun getFriday(): Int?
    fun getSaturday(): Int?
    fun updateDays()
    fun specificOff()
}