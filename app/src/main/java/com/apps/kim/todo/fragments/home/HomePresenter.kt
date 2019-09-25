package com.apps.kim.todo.fragments.home

import android.annotation.SuppressLint
import com.apps.kim.todo.api.Api
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.HistoryDb
import com.apps.kim.todo.db.HistoryDb_
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.utils.PrefProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class HomePresenter(val view: HomeView) {
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)
    private val historyBox = App.boxStore.boxFor(HistoryDb::class.java)

    fun initList(date: Calendar): MutableList<TodoDB> {
        getQuotes()
        val list = mutableListOf<TodoDB>()
        val tag = PrefProvider.mTag ?: TAG_TODO
        val allReminders = reminderBox.query()
            .equal(TodoDB_.requestTag, tag).build().find()

        for (reminder in allReminders) {
            val done =
                "${reminder.title} ${reminder.timeHour} ${reminder.timeMinute} ${date.timeInMillis}"
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = reminder?.startDate ?: EMPTY_LONG
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            val difference = (date.timeInMillis - (calendar.timeInMillis))
            val days = difference / (1000 * 60 * 60 * 24)
            if (date.timeInMillis >= calendar.timeInMillis) {
                if (reminder?.isSpecificDays == true) {
                    if (isTodaySpecificDay(date, reminder)) {
                        if (reminder.isPause == true) {
                            val historyDate = historyBox.query()
                                .equal(HistoryDb_.done, done)
                                .build().find()
                            if (historyDate.size > 0) list.add(reminder)
                        } else list.add(reminder)
                    }
                    Collections.sort(list, TimeComparator())
                } else {
                    if ((days % (reminder.interval ?: 1)) == EMPTY_LONG && difference >= 0) {
                        if (reminder?.isPause == true) {
                            val historyDate = historyBox.query()
                                .equal(HistoryDb_.done, done)
                                .build().find()
                            if (historyDate.size > 0) list.add(reminder)
                        } else list.add(reminder)
                        Collections.sort(list, TimeComparator())
                    }
                }
            }
        }
        return list
    }

    private fun isTodaySpecificDay(calendar: Calendar, reminder: TodoDB): Boolean {
        var isExist = false
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.daySunday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.dayMonday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.dayTuesday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.dayWednesday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.dayThursday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.dayFriday) isExist = true
        if (calendar.get(Calendar.DAY_OF_WEEK) == reminder.daySaturday) isExist = true

        return isExist
    }

    @SuppressLint("CheckResult")
    private fun getQuotes() {
        Api.instance
            .todoApi
            .getQuote()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                PrefProvider.quote =
                    "${data.contents.quotes[0].quote} ${data.contents.quotes[0].author}"
                view.setQuote("${data.contents.quotes[0].quote} ${data.contents.quotes[0].author}")
            }, {
                view.setQuote(PrefProvider.quote)
            })
    }
}