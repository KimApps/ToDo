package com.apps.kim.todo.tools.extensions

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.widget.TextView
import com.apps.kim.todo.app.App

fun TextView.setTextColorCompat(@ColorRes colorRes: Int) {
    setTextColor(ContextCompat.getColor(App.instance, colorRes))
}

fun TextView.getStringText() = this.text.toString()

fun TextView.setTextOrHide(string: String?) {
    this.text = string?.apply {
        if (string.isEmpty()) this@setTextOrHide.hide() else this@setTextOrHide.show()
    }
}
