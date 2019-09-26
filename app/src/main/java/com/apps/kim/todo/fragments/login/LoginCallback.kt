package com.apps.kim.todo.fragments.login

/**
Created by KIM on 26.09.2019
 **/

interface LoginCallback {
    fun googleAuth()
    fun fbAuth()
    fun startMain()
}