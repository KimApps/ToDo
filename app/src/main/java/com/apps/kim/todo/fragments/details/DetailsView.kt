package com.apps.kim.todo.fragments.details

import android.content.SharedPreferences

/**
Created by KIM on 25.09.2019
 **/

interface DetailsView {
    fun setTitle(title: String)
    fun setPeriod(period: String)
    fun setTime1(time: String)
    fun setTime2(time: String)
    fun setTime3(time: String)
    fun setTime4(time: String)
    fun setTime5(time: String)
    fun setTime6(time: String)
    fun setInstruction(instruction: String)
    fun setStatus(status: String)
    fun setStartDate(date: String)
    fun setFinishDate(date: String)
    fun setTaken(taken: String)
    fun timeVisible2(isVisible: Boolean)
    fun timeVisible3(isVisible: Boolean)
    fun timeVisible4(isVisible: Boolean)
    fun timeVisible5(isVisible: Boolean)
    fun timeVisible6(isVisible: Boolean)
}