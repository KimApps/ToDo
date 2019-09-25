package com.apps.kim.todo.tools.utils

import android.annotation.SuppressLint
import com.apps.kim.todo.app.App
import com.apps.kim.todo.tools.classes.*

@SuppressLint("CommitPrefEdits", "ApplySharedPref")
object PrefProvider {

    private val preferences = App.prefs

    fun saveSession(user: String) {
        preferences.edit()
            .putString(PREF_USER_ID, user)
            .putBoolean(PREF_FIRST_OPEN, false)
            .commit()
    }

    fun clearSession() {
        preferences.edit()
            .clear()
            .commit()
    }

    fun remove(tag: String) {
        preferences.edit().remove(tag).commit()
    }


    var isFirstOpen: Boolean
        get() = preferences.getBoolean(PREF_FIRST_OPEN, true)
        set(value) {
            preferences.edit()
                .putBoolean(PREF_FIRST_OPEN, value)
                .commit()
        }


    var userId: String
        get() = preferences.getString(PREF_USER_ID, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_USER_ID, value)
                .commit()
        }

    var periods: String
        get() = preferences.getString(PREF_PERIODS, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_PERIODS, value)
                .commit()
        }

    var mTag: String?
        get() = preferences.getString(PREF_TAG, null)
        set(value) {
            preferences.edit()
                .putString(PREF_TAG, value)
                .commit()
        }

    var quote: String
        get() = preferences.getString(PREF_QUOTE, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_QUOTE, value)
                .commit()
        }

    var sound1: String
        get() = preferences.getString(PREF_SOUND_1, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_1, value)
                .commit()
        }
    var sound2: String
        get() = preferences.getString(PREF_SOUND_2, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_2, value)
                .commit()
        }
    var sound3: String
        get() = preferences.getString(PREF_SOUND_3, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_3, value)
                .commit()
        }
    var sound4: String
        get() = preferences.getString(PREF_SOUND_4, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_4, value)
                .commit()
        }
    var sound5: String
        get() = preferences.getString(PREF_SOUND_5, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_5, value)
                .commit()
        }
    var sound6: String
        get() = preferences.getString(PREF_SOUND_6, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_6, value)
                .commit()
        }

    var soundName1: String
        get() = preferences.getString(PREF_SOUND_NAME_1, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_1, value)
                .commit()
        }

    var soundName2: String
        get() = preferences.getString(PREF_SOUND_NAME_2, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_2, value)
                .commit()
        }
    var soundName3: String
        get() = preferences.getString(PREF_SOUND_NAME_3, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_3, value)
                .commit()
        }
    var soundName4: String
        get() = preferences.getString(PREF_SOUND_NAME_4, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_4, value)
                .commit()
        }
    var soundName5: String
        get() = preferences.getString(PREF_SOUND_NAME_5, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_5, value)
                .commit()
        }
    var soundName6: String
        get() = preferences.getString(PREF_SOUND_NAME_6, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_SOUND_NAME_6, value)
                .commit()
        }

    var startText1: String?
        get() = preferences.getString(PREF_START_TEXT_1, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_1, value)
                .commit()
        }

    var startText2: String?
        get() = preferences.getString(PREF_START_TEXT_2, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_2, value)
                .commit()
        }
    var startText3: String?
        get() = preferences.getString(PREF_START_TEXT_3, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_3, value)
                .commit()
        }
    var startText4: String?
        get() = preferences.getString(PREF_START_TEXT_4, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_4, value)
                .commit()
        }
    var startText5: String?
        get() = preferences.getString(PREF_START_TEXT_5, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_5, value)
                .commit()
        }
    var startText6: String?
        get() = preferences.getString(PREF_START_TEXT_6, null)
        set(value) {
            preferences.edit()
                .putString(PREF_START_TEXT_6, value)
                .commit()
        }

    var startImg1: String?
        get() = preferences.getString(PREF_IMG_1, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_1, value)
                .commit()
        }

    var startImg2: String?
        get() = preferences.getString(PREF_IMG_2, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_2, value)
                .commit()
        }
    var startImg3: String?
        get() = preferences.getString(PREF_IMG_3, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_3, value)
                .commit()
        }
    var startImg4: String?
        get() = preferences.getString(PREF_IMG_4, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_4, value)
                .commit()
        }
    var startImg5: String?
        get() = preferences.getString(PREF_IMG_5, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_5, value)
                .commit()
        }
    var startImg6: String?
        get() = preferences.getString(PREF_IMG_6, null)
        set(value) {
            preferences.edit()
                .putString(PREF_IMG_6, value)
                .commit()
        }

    var backImg1: String?
        get() = preferences.getString(PREF_BACK_1, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_1, value)
                .commit()
        }

    var backImg2: String?
        get() = preferences.getString(PREF_BACK_2, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_2, value)
                .commit()
        }
    var backImg3: String?
        get() = preferences.getString(PREF_BACK_3, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_3, value)
                .commit()
        }
    var backImg4: String?
        get() = preferences.getString(PREF_BACK_4, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_4, value)
                .commit()
        }
    var backImg5: String?
        get() = preferences.getString(PREF_BACK_5, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_5, value)
                .commit()
        }
    var backImg6: String?
        get() = preferences.getString(PREF_BACK_6, null)
        set(value) {
            preferences.edit()
                .putString(PREF_BACK_6, value)
                .commit()
        }


}