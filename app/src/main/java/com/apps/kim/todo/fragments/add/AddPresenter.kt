package com.apps.kim.todo.fragments.add

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.tools.classes.DATA_PENDING_ID
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.classes.PREF_TAG
import com.apps.kim.todo.tools.classes.TAG_ALARM
import com.apps.kim.todo.tools.utils.PrefProvider
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class AddPresenter(val view: AddView) {
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)

    fun saveReminder() {
        if (isAllFieldsCompleted()) {
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
            view.updateDays()
            view.setSpinnerPosition(0)
            view.setSpinnerIntervalPosition(0)
            view.clearTitle()
            view.specificOff()
            App.instance.toast(R.string.success)
            view.startAnim()
        }
    }

    private fun setAlarm(textButton: String) {
        val id = reminderBox.put(getReminder(textButton))
        val alarmManager = view.getAlarmManager()
        val pendingIntent = view.getPendingIntent(id.toInt(), getServiceIntent(id))
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getCalendar(textButton, id).timeInMillis,
                pendingIntent
            )
            else -> alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                getCalendar(textButton, id).timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getCalendar(textButton: String, id: Long): Calendar {
        val currentTime = Calendar.getInstance()
        val startTime = getStartDate()
        startTime.set(Calendar.HOUR_OF_DAY, getHour(textButton))
        startTime.set(Calendar.MINUTE, getMinute(textButton))
        startTime.set(Calendar.SECOND, 0)
        while (currentTime > startTime) startTime.add(Calendar.DATE, getPeriod())
        return startTime
    }

    private fun getServiceIntent(id: Long): Intent {
        val intent = view.getServiceIntent(id.toInt())
        intent.putExtra(DATA_PENDING_ID, id)
        return intent
    }

    private fun isAllFieldsCompleted(): Boolean {
        var isSave = true
        if (view.getTitle().isEmpty()) {
            isSave = false
            App.instance.toast(R.string.set_title)
        } else {
            for (name in getMedicineList()) {
                if (name == view.getTitle()) {
                    isSave = false
                    App.instance.toast(R.string.title_exist)
                }
            }
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

    private fun getReminder(textButton: String): TodoDB {
        return TodoDB(
            view.getTitle(),
            view.getSpinnerPosition() + 1,
            getPeriod(),
            getStartDate().time.time,
            getTag(),
            getHour(textButton),
            getMinute(textButton),
            false,
            view.isSpecificDays(),
            view.getSunday(),
            view.getMonday(),
            view.getTuesday(),
            view.getWednesday(),
            view.getThursday(),
            view.getFriday(),
            view.getSaturday()
        )
    }

    private fun getTag(): String = PrefProvider.mTag ?: TAG_ALARM

    private fun getHour(textButton: String): Int = (textButton.split(":"))[0].toInt()
    private fun getMinute(textButton: String): Int = (textButton.split(":"))[1].toInt()
    private fun getStartDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (view.getStartDate() != App.instance.getStringApp(R.string.today)) {
            val start = view.getStartDate().split(".")
            calendar.set(Calendar.YEAR, start[0].toInt())
            calendar.set(Calendar.MONTH, (start[1].toInt()) - 1)
            calendar.set(Calendar.DAY_OF_MONTH, start[2].toInt())
        }
        return calendar
    }

    private fun getPeriod(): Int {
        return when (view.getSpinnerIntervalPosition()) {
            1 -> 2
            2 -> 3
            3 -> 7
            else -> 1
        }
    }

    fun showButtons(position: Int) {
        when (position) {
            0 -> {
                view.hideButton2()
                view.hideButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
            }
            1 -> {
                view.showButton2()
                view.hideButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
            }
            2 -> {
                view.showButton2()
                view.showButton3()
                view.hideButton4()
                view.hideButton5()
                view.hideButton6()
            }
            3 -> {
                view.showButton2()
                view.showButton3()
                view.showButton4()
                view.hideButton5()
                view.hideButton6()
            }
            4 -> {
                view.showButton2()
                view.showButton3()
                view.showButton4()
                view.showButton5()
                view.hideButton6()
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