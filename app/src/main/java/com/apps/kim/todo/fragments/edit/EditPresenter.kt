package com.apps.kim.todo.fragments.edit

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.classes.DATA_PENDING_ID
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.classes.PREF_TAG
import com.apps.kim.todo.tools.classes.TAG_ALARM
import com.apps.kim.todo.tools.utils.PrefProvider
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class EditPresenter(val view: EditView) {
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)
    private lateinit var reminderList: List<TodoDB>
    private var startDate: Date? = null

    fun initView(title: String) {
        val tag = PrefProvider.mTag ?: TAG_ALARM
        val reminders = reminderBox
            .query()
            .equal(TodoDB_.requestTag, tag)
            .equal(TodoDB_.title, title)
            .build()
            .find()
        reminderList = reminders
        val reminder = reminders[0]
        startDate = reminder.startDate
        view.setTitle(reminder.title ?: EMPTY_STRING)
        view.setSpinnerPosition(reminders.size - 1)
        view.setSpinnerIntervalPosition(setPeriod(reminder.interval ?: 0))
        if (reminder.isSpecificDays == true) {
            view.setSpecificDays(true)
            view.setSunday((reminder.daySunday ?: 0) > 0)
            view.setMonday((reminder.dayMonday ?: 0) > 0)
            view.setTuesday((reminder.dayTuesday ?: 0) > 0)
            view.setWednesday((reminder.dayWednesday ?: 0) > 0)
            view.setThursday((reminder.dayThursday ?: 0) > 0)
            view.setFriday((reminder.dayFriday ?: 0) > 0)
            view.setSaturday((reminder.daySaturday ?: 0) > 0)
        } else view.setSpecificDays(false)
        if (view.getLiveData1().isNullOrEmpty()) view.setLiveData1(
            "${reminder.timeHour}:${getMinute(
                reminder.timeMinute ?: 0
            )}"
        )
        if (reminders.size > 1 && view.getLiveData2().isNullOrEmpty()) view.setLiveData2(
            "${reminders[1].timeHour}:${getMinute(
                reminders[1].timeMinute ?: 0
            )}"
        )
        if (reminders.size > 2 && view.getLiveData3().isNullOrEmpty()) view.setLiveData3(
            "${reminders[2].timeHour}:${getMinute(
                reminders[2].timeMinute ?: 0
            )}"
        )
        if (reminders.size > 3 && view.getLiveData4().isNullOrEmpty()) view.setLiveData4(
            "${reminders[3].timeHour}:${getMinute(
                reminders[3].timeMinute ?: 0
            )}"
        )
        if (reminders.size > 4 && view.getLiveData5().isNullOrEmpty()) view.setLiveData5(
            "${reminders[4].timeHour}:${getMinute(
                reminders[4].timeMinute ?: 0
            )}"
        )
        if (reminders.size > 5 && view.getLiveData6().isNullOrEmpty()) view.setLiveData6(
            "${reminders[5].timeHour}:${getMinute(
                reminders[5].timeMinute ?: 0
            )}"
        )
        if (!reminder.instruction.isNullOrEmpty()) view.setInstruction(
            reminder.instruction ?: EMPTY_STRING
        )
    }

    fun saveReminder() {
        if (isAllFieldsCompleted()) {
            reminderBox.query()
                .equal(TodoDB_.title, reminderList[0].title ?: EMPTY_STRING)
                .build()
                .remove()
            if (view.getTextButton1() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton1()
            )
            if (view.getTextButton2() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton2()
            )
            if (view.getTextButton3() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton3()
            )
            if (view.getTextButton4() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton4()
            )
            if (view.getTextButton5() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton5()
            )
            if (view.getTextButton6() != App.instance.getStringApp(R.string.pick_a_time)) setAlarm(
                view.getTextButton6()
            )
            view.clearLiveData()
            App.instance.toast(R.string.success)
            view.goBack()
        }
    }

    private fun getReminder(textButton: String): TodoDB {
        val rem = reminderList[0]
        return TodoDB(
            view.getTitle(),
            view.getSpinnerPosition() + 1,
            getPeriod(),
            startDate,
            getTag(),
            getHour(textButton),
            getMinute(textButton),
            rem.isPause,
            view.isSpecificDays(),
            view.getSunday(),
            view.getMonday(),
            view.getTuesday(),
            view.getWednesday(),
            view.getThursday(),
            view.getFriday(),
            view.getSaturday(),
            view.getInstruction(),
            rem.pendingId,
            rem.finishDate

        ).also {
            it.history = rem.history
        }
    }

    private fun setAlarm(textButton: String) {
        val id = reminderBox.put(getReminder(textButton))
        val alarmManager = view.getAlarmManager()
        val pendingIntent = view.getPendingIntent(id.toInt(), getServiceIntent(id))
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getCalendar(textButton).timeInMillis,
                pendingIntent
            )
            else -> alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                getCalendar(textButton).timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getCalendar(textButton: String): Calendar {
        val currentTime = Calendar.getInstance()
        val startTime = Calendar.getInstance()
        startTime.time = startDate
        startTime.set(Calendar.HOUR_OF_DAY, getHour(textButton))
        startTime.set(Calendar.MINUTE, getMinute(textButton))
        startTime.set(Calendar.SECOND, 0)
        while (currentTime > startTime) {
            startTime.add(Calendar.DATE, getPeriod())
        }
        return startTime
    }

    private fun getServiceIntent(id: Long): Intent {
        val intent = view.getServiceIntent(id.toInt())
        intent.putExtra(DATA_PENDING_ID, id)
        return intent
    }

    private fun getPeriod(): Int {
        return when (view.getSpinnerIntervalPosition()) {
            1 -> 2
            2 -> 3
            3 -> 7
            else -> 1
        }
    }

    private fun setPeriod(period: Int): Int {
        return when (period) {
            2 -> 1
            3 -> 2
            7 -> 3
            else -> 0
        }
    }

    private fun getMinute(minute: Int): String {
        return if (minute < 10) "0$minute"
        else minute.toString()
    }

    private fun getTag(): String = PrefProvider.mTag ?: TAG_ALARM

    private fun getHour(textButton: String): Int = (textButton.split(":"))[0].toInt()
    private fun getMinute(textButton: String): Int = (textButton.split(":"))[1].toInt()

    private fun isAllFieldsCompleted(): Boolean {
        var isSave = true
        if (view.getTitle().isEmpty()) {
            isSave = false
            App.instance.toast(R.string.set_title)
        } else {
            if (view.isSpecificDays()) {
                if (weekDaysSum() == 0) {
                    isSave = false
                    App.instance.toast(R.string.select_day)
                }
            }
            if (isSave) {
                when (view.getSpinnerPosition()) {
                    0 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time)) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                    1 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton2() == App.instance.getStringApp(R.string.pick_a_time)
                        ) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                    2 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton2() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton3() == App.instance.getStringApp(R.string.pick_a_time)
                        ) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                    3 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton2() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton3() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton4() == App.instance.getStringApp(R.string.pick_a_time)
                        ) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                    4 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton2() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton3() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton4() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton5() == App.instance.getStringApp(R.string.pick_a_time)
                        ) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                    5 -> {
                        if (view.getTextButton1() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton2() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton3() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton4() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton5() == App.instance.getStringApp(R.string.pick_a_time) ||
                            view.getTextButton6() == App.instance.getStringApp(R.string.pick_a_time)
                        ) {
                            isSave = false
                            App.instance.toast(R.string.not_all_reminder)
                        }
                    }
                }
            }

        }
        return isSave
    }

    private fun getMedicineList(): List<String> {
        val reminders = reminderBox.all
        val list = mutableSetOf<String>()
        for (reminder in reminders) {
            list.add(reminder.title ?: EMPTY_STRING)
        }
        return list.toList().sorted()
    }

    fun showButtons(position: Int) {
        when (position) {
            0 -> {
                view.hideButton2()
                view.hideButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
                view.setLiveData2(EMPTY_STRING)
                view.setLiveData3(EMPTY_STRING)
                view.setLiveData4(EMPTY_STRING)
                view.setLiveData5(EMPTY_STRING)
                view.setLiveData6(EMPTY_STRING)
            }
            1 -> {
                view.showButton2()
                view.hideButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
                view.setLiveData3(EMPTY_STRING)
                view.setLiveData4(EMPTY_STRING)
                view.setLiveData5(EMPTY_STRING)
                view.setLiveData6(EMPTY_STRING)
            }
            2 -> {
                view.showButton2()
                view.showButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
                view.setLiveData4(EMPTY_STRING)
                view.setLiveData5(EMPTY_STRING)
                view.setLiveData6(EMPTY_STRING)
            }
            3 -> {
                view.showButton2()
                view.showButton3()
                view.showButton4()
                view.hideButton5()
                view.hideButton6()
                view.setLiveData5(EMPTY_STRING)
                view.setLiveData6(EMPTY_STRING)
            }
            4 -> {
                view.showButton2()
                view.showButton3()
                view.showButton4()
                view.showButton5()
                view.hideButton6()
                view.setLiveData6(EMPTY_STRING)
            }
            5 -> {
                view.showButton2()
                view.showButton3()
                view.showButton4()
                view.showButton5()
                view.showButton6()
            }
        }

    }

    private fun weekDaysSum(): Int {
        return (view.getSunday() ?: 0) +
                (view.getMonday() ?: 0) +
                (view.getTuesday() ?: 0) +
                (view.getWednesday() ?: 0) +
                (view.getThursday() ?: 0) +
                (view.getFriday() ?: 0) +
                (view.getSaturday() ?: 0)
    }

}