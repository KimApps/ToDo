package com.apps.kim.todo.fragments.edit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences

/**
Created by KIM on 25.09.2019
 **/

interface EditView {
    fun clearLiveData()
    fun setLiveData1(text: String)
    fun setLiveData2(text: String)
    fun setLiveData3(text: String)
    fun setLiveData4(text: String)
    fun setLiveData5(text: String)
    fun setLiveData6(text: String)
    fun getLiveData1(): String?
    fun getLiveData2(): String?
    fun getLiveData3(): String?
    fun getLiveData4(): String?
    fun getLiveData5(): String?
    fun getLiveData6(): String?
    fun setTitle(title: String)
    fun setInstruction(instruction: String)
    fun getInstruction(): String
    fun getTitle(): String
    fun setSpinnerPosition(position: Int)
    fun getSpinnerPosition(): Int
    fun setSpinnerIntervalPosition(position: Int)
    fun getSpinnerIntervalPosition(): Int
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
    fun goBack()
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