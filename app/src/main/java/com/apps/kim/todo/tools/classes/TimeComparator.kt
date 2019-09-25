package com.apps.kim.todo.tools.classes

import com.apps.kim.todo.db.TodoDB

/**
Created by KIM on 25.09.2019
 **/

class TimeComparator : Comparator<TodoDB> {
    override fun compare(reminder1: TodoDB, reminder2: TodoDB): Int {
        val first = "${reminder1.timeHour}${getMinutes(reminder1.timeMinute)}"
        val second = "${reminder2.timeHour}${getMinutes(reminder2.timeMinute)}"
        return first.toInt() - second.toInt()
    }

    private fun getMinutes(timeMinute: Int?): String {
        return if (timeMinute != null) {
            if (timeMinute < 10) "0$timeMinute"
            else timeMinute.toString()
        } else EMPTY_STRING
    }

}