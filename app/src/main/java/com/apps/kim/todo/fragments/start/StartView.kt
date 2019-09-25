package com.apps.kim.todo.fragments.start

import android.content.SharedPreferences

/**
Created by KIM on 25.09.2019
 **/

interface StartView {
    fun setBtnImg1(path: String)
    fun setBtnImg2(path: String)
    fun setBtnImg3(path: String)
    fun setBtnImg4(path: String)
    fun setBtnImg5(path: String)
    fun setBtnImg6(path: String)
    fun setBtnText1(text: String)
    fun setBtnText2(text: String)
    fun setBtnText3(text: String)
    fun setBtnText4(text: String)
    fun setBtnText5(text: String)
    fun setBtnText6(text: String)
}