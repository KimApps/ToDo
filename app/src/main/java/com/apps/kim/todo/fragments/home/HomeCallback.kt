package com.apps.kim.todo.fragments.home

import android.os.Bundle

/**
Created by KIM on 25.09.2019
 **/

interface HomeCallback {
    fun showDetailsFragment(bundle: Bundle)
    fun menuVisibility(isVisible: Boolean)
}