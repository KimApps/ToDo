package com.apps.kim.todo.fragments.customize

import android.content.SharedPreferences

/**
Created by KIM on 25.09.2019
 **/

interface CustomizeView {
    fun setBtnText(text: String)
    fun getBtnText(): String
    fun getStringResource(id: Int): String
    fun goBack()
    fun setBtnColorImage(colorId: Int)
    fun setBtnColorBack(colorId: Int)
    fun setBtnColorImageDefault(colorId: Int)
    fun setBtnColorBackDefault(colorId: Int)
    fun setTextColorImageDefault(colorId: Int)
    fun setTextColorBackDefault(colorId: Int)
    fun setIconImage(path: String)
    fun setIconBack(path: String)
    fun setAudioFileName(name: String)
}