package com.apps.kim.todo.tools.utils

import android.content.Context
import com.apps.kim.todo.app.App


object LogoutUtils {
    fun doLogout(context: Context = App.instance) {
        PrefProvider.clearSession()
        DataBaseProvider.deleteTable()
    }
}