package com.apps.kim.todo.fragments.start

/**
Created by KIM on 25.09.2019
 **/

interface StartCallback {
    fun menuVisibility(isVisible: Boolean)
    fun changeBackground(path: String)
    fun showHomeFragment()
}