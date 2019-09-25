package com.apps.kim.todo.tools.extensions

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.getInteger(intRes: Int) = this.resources.getInteger(intRes)

fun Context.getContextCompatColor(colorRes: Int) = ContextCompat.getColor(this, colorRes)
