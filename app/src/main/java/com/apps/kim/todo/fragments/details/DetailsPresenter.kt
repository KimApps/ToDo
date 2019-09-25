package com.apps.kim.todo.fragments.details

import android.text.format.DateFormat
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.HistoryDb
import com.apps.kim.todo.db.HistoryDb_
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.tools.classes.EMPTY_LONG
import com.apps.kim.todo.tools.classes.EMPTY_STRING

/**
Created by KIM on 25.09.2019
 **/

class DetailsPresenter(val view: DetailsView) {

    fun initView(reminders: List<TodoDB>) {
        val reminder = reminders[0]
        setTimes(reminders)
        view.setTitle(reminder.title ?: EMPTY_STRING)
        view.setInstruction(
            reminder.instruction ?: App.instance.getStringApp(R.string.instructions_not_specified)
        )
        view.setStartDate(
            DateFormat.format(
                "yyyy MMM d",
                reminder.startDate ?: EMPTY_LONG
            ).toString()
        )
        if (reminder.isSpecificDays == true) {
            var string = EMPTY_STRING
            if (reminder.daySunday != null) string += "Sunday\n"
            if (reminder.dayMonday != null) string += "Monday\n"
            if (reminder.dayTuesday != null) string += "Tuesday\n"
            if (reminder.dayWednesday != null) string += "Wednesday\n"
            if (reminder.dayThursday != null) string += "Thursday\n"
            if (reminder.dayFriday != null) string += "Friday\n"
            if (reminder.daySaturday != null) string += "Saturday\n"
            view.setPeriod(string)
        } else {
            view.setPeriod(
                when (reminder.interval) {
                    1 -> "Every\n day"
                    else -> "Every\n ${reminder.interval} days"
                }
            )
        }
        view.setTaken(getTaken("${reminder.title}"))
        view.setStatus(
            when (reminder.isPause) {
                true -> App.instance.getStringApp(R.string.stopped)
                else -> App.instance.getStringApp(R.string.process)
            }
        )
    }

    private fun getTaken(title: String): String {
        val historyBox = App.boxStore.boxFor(HistoryDb::class.java)
        val query = historyBox.query()
            .equal(HistoryDb_.title, title)
            .build().find()
        return query.size.toString()
    }

    private fun setTimes(reminders: List<TodoDB>) {
        when (reminders.size) {
            1 -> view.setTime1(
                "${reminders[0].timeHour}:${getMinute(
                    reminders[0].timeMinute ?: 0
                )}"
            )
            2 -> {
                view.setTime1("${reminders[0].timeHour}:${getMinute(reminders[0].timeMinute ?: 0)}")
                view.timeVisible2(true)
                view.setTime2("${reminders[1].timeHour}:${getMinute(reminders[1].timeMinute ?: 0)}")
            }
            3 -> {
                view.setTime1("${reminders[0].timeHour}:${getMinute(reminders[0].timeMinute ?: 0)}")
                view.timeVisible2(true)
                view.setTime2("${reminders[1].timeHour}:${getMinute(reminders[1].timeMinute ?: 0)}")
                view.timeVisible3(true)
                view.setTime3("${reminders[2].timeHour}:${getMinute(reminders[2].timeMinute ?: 0)}")
            }
            4 -> {
                view.setTime1("${reminders[0].timeHour}:${getMinute(reminders[0].timeMinute ?: 0)}")
                view.timeVisible2(true)
                view.setTime2("${reminders[1].timeHour}:${getMinute(reminders[1].timeMinute ?: 0)}")
                view.timeVisible3(true)
                view.setTime3("${reminders[2].timeHour}:${getMinute(reminders[2].timeMinute ?: 0)}")
                view.timeVisible4(true)
                view.setTime4("${reminders[3].timeHour}:${getMinute(reminders[3].timeMinute ?: 0)}")
            }
            5 -> {
                view.setTime1("${reminders[0].timeHour}:${getMinute(reminders[0].timeMinute ?: 0)}")
                view.timeVisible2(true)
                view.setTime2("${reminders[1].timeHour}:${getMinute(reminders[1].timeMinute ?: 0)}")
                view.timeVisible3(true)
                view.setTime3("${reminders[2].timeHour}:${getMinute(reminders[2].timeMinute ?: 0)}")
                view.timeVisible4(true)
                view.setTime4("${reminders[3].timeHour}:${getMinute(reminders[3].timeMinute ?: 0)}")
                view.timeVisible5(true)
                view.setTime5("${reminders[4].timeHour}:${getMinute(reminders[4].timeMinute ?: 0)}")
            }
            6 -> {
                view.setTime1("${reminders[0].timeHour}:${getMinute(reminders[0].timeMinute ?: 0)}")
                view.timeVisible2(true)
                view.setTime2("${reminders[1].timeHour}:${getMinute(reminders[1].timeMinute ?: 0)}")
                view.timeVisible3(true)
                view.setTime3("${reminders[2].timeHour}:${getMinute(reminders[2].timeMinute ?: 0)}")
                view.timeVisible4(true)
                view.setTime4("${reminders[3].timeHour}:${getMinute(reminders[3].timeMinute ?: 0)}")
                view.timeVisible5(true)
                view.setTime5("${reminders[4].timeHour}:${getMinute(reminders[4].timeMinute ?: 0)}")
                view.timeVisible6(true)
                view.setTime6("${reminders[5].timeHour}:${getMinute(reminders[5].timeMinute ?: 0)}")
            }
        }
    }

    private fun getMinute(minute: Int): String {
        return if (minute < 10) "0$minute"
        else minute.toString()
    }
}