package com.apps.kim.todo.fragments.start

import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.utils.PrefProvider

/**
Created by KIM on 25.09.2019
 **/

class StartPresenter(private val view: StartView) {
    private val uri = "android.resource://com.apps.kim.todo/"
    fun init() {
        view.setBtnImg1(PrefProvider.startImg1 ?: uri + R.drawable.btn_alarm)
        view.setBtnImg2(PrefProvider.startImg2 ?: uri + R.drawable.btn_family)
        view.setBtnImg3(PrefProvider.startImg3 ?: uri + R.drawable.btn_friend)
        view.setBtnImg4(PrefProvider.startImg4 ?: uri + R.drawable.btn_workout)
        view.setBtnImg5(PrefProvider.startImg5 ?: uri + R.drawable.btn_medicines)
        view.setBtnImg6(PrefProvider.startImg6 ?: uri + R.drawable.btn_diet)
        view.setBtnText1(PrefProvider.startText1 ?: App.instance.getStringApp(R.string.alarms))
        view.setBtnText2(PrefProvider.startText2 ?: App.instance.getStringApp(R.string.family))
        view.setBtnText3(PrefProvider.startText3 ?: App.instance.getStringApp(R.string.friends))
        view.setBtnText4(PrefProvider.startText4 ?: App.instance.getStringApp(R.string.workout))
        view.setBtnText5(PrefProvider.startText5 ?: App.instance.getStringApp(R.string.medicines))
        view.setBtnText6(PrefProvider.startText6 ?: App.instance.getStringApp(R.string.diet))
    }

    fun getPath(button: String): String {
        var path = EMPTY_STRING
        when (button) {
            BUTTON_1 -> path = PrefProvider.backImg1 ?: uri + R.drawable.back_alarm
            BUTTON_2 -> path = PrefProvider.backImg2 ?: uri + R.drawable.back_family
            BUTTON_3 -> path = PrefProvider.backImg3 ?: uri + R.drawable.back_friend
            BUTTON_4 -> path = PrefProvider.backImg4 ?: uri + R.drawable.back_workout
            BUTTON_5 -> path = PrefProvider.backImg5 ?: uri + R.drawable.back_medicines
            BUTTON_6 -> path = PrefProvider.backImg6 ?: uri + R.drawable.back_diet
        }
        return path
    }
}