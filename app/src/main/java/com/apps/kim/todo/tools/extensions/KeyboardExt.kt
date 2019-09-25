package com.apps.kim.todo.tools.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


internal val NO_FLAGS = 0

fun Activity.hideKeyboard() = currentFocus?.let {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(it.windowToken, NO_FLAGS)
    }
}

fun androidx.fragment.app.Fragment.hideKeyboard() = activity?.hideKeyboard()

fun android.app.Fragment.hideKeyboard() = activity.hideKeyboard()


