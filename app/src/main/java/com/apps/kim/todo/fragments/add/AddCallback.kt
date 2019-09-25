package com.apps.kim.todo.fragments.add

import android.os.Bundle

/**
Created by KIM on 25.09.2019
 **/

interface AddCallback {
    fun showDatePickFragment()
    fun showTimePickFragment(bundle: Bundle)
    fun menuVisibility(isVisible: Boolean)
}