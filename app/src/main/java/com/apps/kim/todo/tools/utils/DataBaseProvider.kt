package com.apps.kim.todo.tools.utils

import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB

object DataBaseProvider {
    private val exBox = App.boxStore.boxFor(TodoDB::class.java)


    fun deleteTable() = exBox.removeAll()


}