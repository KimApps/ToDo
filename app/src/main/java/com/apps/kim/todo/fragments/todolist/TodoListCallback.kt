package com.apps.kim.todo.fragments.todolist

import android.os.Bundle

/**
Created by KIM on 25.09.2019
 **/

interface TodoListCallback {
    fun showDetailsFragment(bundle: Bundle)
    fun showEditFragment(bundle: Bundle)
    fun menuVisibility(isVisible: Boolean)
}