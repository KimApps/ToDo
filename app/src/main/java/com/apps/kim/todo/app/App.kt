package com.apps.kim.todo.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.multidex.MultiDex
import com.apps.kim.todo.BuildConfig
import com.apps.kim.todo.db.MyObjectBox
import com.apps.kim.todo.tools.classes.APP_PREFERENCES
import com.securepreferences.SecurePreferences
import io.objectbox.BoxStore

/**
Created by KIM on 25.09.2019
 **/

class App: Application() {
    companion object {
        lateinit var instance: App
            private set
        lateinit var prefs: SharedPreferences
        lateinit var boxStore: BoxStore
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        boxStore = MyObjectBox.builder().androidContext(this).build()
        if (BuildConfig.DEBUG) prefs = getSharedPreferences()
        else prefs = getSecurePreferences()
    }

    private fun getSecurePreferences() =
        SecurePreferences(this, BuildConfig.SECURE_PREF_PASSWORD, APP_PREFERENCES)

    private fun getSharedPreferences() =
        instance.applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    fun toast(resId: Int) = Toast.makeText(this, getStringApp(resId), Toast.LENGTH_LONG).show()
    fun getStringApp(@StringRes stringId: Int) = instance.getString(stringId)
    fun getDrawableApp(drawableId: Int) = instance.getDrawable(drawableId)
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}