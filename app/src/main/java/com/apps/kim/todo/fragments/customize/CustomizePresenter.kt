package com.apps.kim.todo.fragments.customize

import android.content.Intent
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.utils.PrefProvider
import com.vincent.filepicker.Constant
import com.vincent.filepicker.filter.entity.AudioFile

/**
Created by KIM on 25.09.2019
 **/

class CustomizePresenter(val view: CustomizeView) {
    private val uri = "android.resource://com.apps.kim.todo/"
    private var soundPath = EMPTY_STRING
    private var soundName = EMPTY_STRING
    private var pathImage1 = EMPTY_STRING
    private var pathImage2 = EMPTY_STRING
    private var pathImage3 = EMPTY_STRING
    private var pathImage4 = EMPTY_STRING
    private var pathImage5 = EMPTY_STRING
    private var pathImage6 = EMPTY_STRING
    private var pathBack1 = EMPTY_STRING
    private var pathBack2 = EMPTY_STRING
    private var pathBack3 = EMPTY_STRING
    private var pathBack4 = EMPTY_STRING
    private var pathBack5 = EMPTY_STRING
    private var pathBack6 = EMPTY_STRING

    fun init(button: String) {
        val defaultBtnText1 = view.getStringResource(R.string.alarms)
        val defaultBtnText2 = view.getStringResource(R.string.family)
        val defaultBtnText3 = view.getStringResource(R.string.friends)
        val defaultBtnText4 = view.getStringResource(R.string.workout)
        val defaultBtnText5 = view.getStringResource(R.string.medicines)
        val defaultBtnText6 = view.getStringResource(R.string.diet)
        when (button) {
            BUTTON_1 -> {
                view.setBtnText(PrefProvider.startText1 ?: defaultBtnText1)
                view.setIconBack(PrefProvider.backImg1 ?: uri + R.drawable.back_alarm)
                view.setIconImage(PrefProvider.startImg1 ?: uri + R.drawable.btn_alarm)
                view.setAudioFileName(PrefProvider.soundName1)
            }
            BUTTON_2 -> {
                view.setBtnText(PrefProvider.startText2 ?: defaultBtnText2)
                view.setIconBack(PrefProvider.backImg2 ?: uri + R.drawable.back_family)
                view.setIconImage(PrefProvider.startImg2 ?: uri + R.drawable.btn_family)
                view.setAudioFileName(PrefProvider.soundName2)
            }
            BUTTON_3 -> {
                view.setBtnText(PrefProvider.startText3 ?: defaultBtnText3)
                view.setIconBack(PrefProvider.backImg3 ?: uri + R.drawable.back_friend)
                view.setIconImage(PrefProvider.startImg3 ?: uri + R.drawable.btn_friend)
                view.setAudioFileName(PrefProvider.soundName3)
            }
            BUTTON_4 -> {
                view.setBtnText(PrefProvider.startText4 ?: defaultBtnText4)
                view.setIconBack(PrefProvider.backImg4 ?: uri + R.drawable.back_workout)
                view.setIconImage(PrefProvider.startImg4 ?: uri + R.drawable.btn_workout)
                view.setAudioFileName(PrefProvider.soundName4)
            }
            BUTTON_5 -> {
                view.setBtnText(PrefProvider.startText5 ?: defaultBtnText5)
                view.setIconBack(PrefProvider.backImg5 ?: uri + R.drawable.back_medicines)
                view.setIconImage(PrefProvider.startImg5 ?: uri + R.drawable.btn_medicines)
                view.setAudioFileName(PrefProvider.soundName5)
            }
            BUTTON_6 -> {
                view.setBtnText(PrefProvider.startText6 ?: defaultBtnText6)
                view.setIconBack(PrefProvider.backImg6 ?: uri + R.drawable.back_diet)
                view.setIconImage(PrefProvider.startImg6 ?: uri + R.drawable.btn_diet)
                view.setAudioFileName(PrefProvider.soundName6)
            }
        }
    }

    fun save(button: String) {
        if (ifComplete()) {
            when (button) {
                BUTTON_1 -> {
                    PrefProvider.startText1 = view.getBtnText()
                    PrefProvider.sound1 = soundPath
                    PrefProvider.soundName1 = soundName
                }
                BUTTON_2 -> {
                    PrefProvider.startText2 = view.getBtnText()
                    PrefProvider.sound2 = soundPath
                    PrefProvider.soundName2 = soundName
                }
                BUTTON_3 -> {
                    PrefProvider.startText3 = view.getBtnText()
                    PrefProvider.sound3 = soundPath
                    PrefProvider.soundName3 = soundName
                }
                BUTTON_4 -> {
                    PrefProvider.startText4 = view.getBtnText()
                    PrefProvider.sound4 = soundPath
                    PrefProvider.soundName4 = soundName
                }
                BUTTON_5 -> {
                    PrefProvider.startText5 = view.getBtnText()
                    PrefProvider.sound5 = soundPath
                    PrefProvider.soundName5 = soundName
                }
                BUTTON_6 -> {
                    PrefProvider.startText6 = view.getBtnText()
                    PrefProvider.sound6 = soundPath
                    PrefProvider.soundName6 = soundName
                }
            }
            if (pathBack1.isNotEmpty()) PrefProvider.backImg1 = pathBack1
            if (pathBack2.isNotEmpty()) PrefProvider.backImg2 = pathBack2
            if (pathBack3.isNotEmpty()) PrefProvider.backImg3 = pathBack3
            if (pathBack4.isNotEmpty()) PrefProvider.backImg4 = pathBack4
            if (pathBack5.isNotEmpty()) PrefProvider.backImg5 = pathBack5
            if (pathBack6.isNotEmpty()) PrefProvider.backImg6 = pathBack6
            if (pathImage1.isNotEmpty()) PrefProvider.startImg1 = pathImage1
            if (pathImage2.isNotEmpty()) PrefProvider.startImg2 = pathImage2
            if (pathImage3.isNotEmpty()) PrefProvider.startImg3 = pathImage3
            if (pathImage4.isNotEmpty()) PrefProvider.startImg4 = pathImage4
            if (pathImage5.isNotEmpty()) PrefProvider.startImg5 = pathImage5
            if (pathImage6.isNotEmpty()) PrefProvider.startImg6 = pathImage6
            view.goBack()
        }
    }

    fun setDefBackground(button: String) {
        when (button) {
            BUTTON_1 -> {
                PrefProvider.remove(PREF_BACK_1)
                pathBack1 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_medicines)
            }
            BUTTON_2 -> {
                PrefProvider.remove(PREF_BACK_2)
                pathBack2 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_family)
            }
            BUTTON_3 -> {
                PrefProvider.remove(PREF_BACK_3)
                pathBack3 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_friend)
            }
            BUTTON_4 -> {
                PrefProvider.remove(PREF_BACK_4)
                pathBack4 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_workout)
            }
            BUTTON_5 -> {
                PrefProvider.remove(PREF_BACK_5)
                pathBack5 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_medicines)
            }
            BUTTON_6 -> {
                PrefProvider.remove(PREF_BACK_6)
                pathBack6 = EMPTY_STRING
                view.setIconBack(uri + R.drawable.back_diet)
            }
        }
    }

    fun setDefImage(button: String) {
        when (button) {
            BUTTON_1 -> {
                PrefProvider.remove(PREF_IMG_1)
                pathImage1 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_alarm)
            }
            BUTTON_2 -> {
                PrefProvider.remove(PREF_IMG_2)
                pathImage2 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_family)
            }
            BUTTON_3 -> {
                PrefProvider.remove(PREF_IMG_3)
                pathImage3 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_friend)
            }
            BUTTON_4 -> {
                PrefProvider.remove(PREF_IMG_4)
                pathImage4 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_workout)
            }
            BUTTON_5 -> {
                PrefProvider.remove(PREF_IMG_5)
                pathImage5 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_medicines)
            }
            BUTTON_6 -> {
                PrefProvider.remove(PREF_IMG_6)
                pathImage6 = EMPTY_STRING
                view.setIconImage(uri + R.drawable.btn_diet)
            }
        }
    }

    private fun ifComplete(): Boolean = view.getBtnText().isNotEmpty()

    fun onGalleryImageResult(data: Intent, requestCode: Int) {
        val path = data.extras.getStringArrayList(KEY_DATA_RESULT)[0] ?: EMPTY_STRING
        when (requestCode) {
            REQUEST_GALLERY_IMG1 -> {
                pathImage1 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_IMG2 -> {
                pathImage2 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_IMG3 -> {
                pathImage3 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_IMG4 -> {
                pathImage4 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_IMG5 -> {
                pathImage5 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_IMG6 -> {
                pathImage6 = path
                view.setIconImage(path)
            }

            REQUEST_GALLERY_BACK1 -> {
                pathBack1 = path
                view.setIconBack(path)
            }
            REQUEST_GALLERY_BACK2 -> {
                pathBack2 = path
                view.setIconBack(path)
            }

            REQUEST_GALLERY_BACK3 -> {
                pathBack3 = path
                view.setIconBack(path)
            }

            REQUEST_GALLERY_BACK4 -> {
                pathBack4 = path
                view.setIconBack(path)
            }

            REQUEST_GALLERY_BACK5 -> {
                pathBack5 = path
                view.setIconBack(path)
            }

            REQUEST_GALLERY_BACK6 -> {
                pathBack6 = path
                view.setIconBack(path)
            }
        }
    }

    fun setAudio(buttonNumber: String, intent: Intent?) {
        val audio = intent?.getParcelableArrayListExtra<AudioFile>(Constant.RESULT_PICK_AUDIO)
        val path = audio?.get(0)?.path
        soundName = audio?.get(0)?.name ?: EMPTY_STRING
        view.setAudioFileName(soundName)
        when (buttonNumber) {
            BUTTON_1 -> soundPath = path ?: EMPTY_STRING
            BUTTON_2 -> soundPath = path ?: EMPTY_STRING
            BUTTON_3 -> soundPath = path ?: EMPTY_STRING
            BUTTON_4 -> soundPath = path ?: EMPTY_STRING
            BUTTON_5 -> soundPath = path ?: EMPTY_STRING
            BUTTON_6 -> soundPath = path ?: EMPTY_STRING
        }
    }

    fun setDefaultAudio(buttonNumber: String) {
        view.setAudioFileName(EMPTY_STRING)
        soundName = EMPTY_STRING
        when (buttonNumber) {
            BUTTON_1 -> soundPath = EMPTY_STRING
            BUTTON_2 -> soundPath = EMPTY_STRING
            BUTTON_3 -> soundPath = EMPTY_STRING
            BUTTON_4 -> soundPath = EMPTY_STRING
            BUTTON_5 -> soundPath = EMPTY_STRING
            BUTTON_6 -> soundPath = EMPTY_STRING
        }
    }

    fun getTag(): String {
        return when (PrefProvider.mTag?: TAG_ALARM) {
            TAG_FAMILY -> BUTTON_2
            TAG_FRIENDS -> BUTTON_3
            TAG_WORKOUT -> BUTTON_4
            TAG_MEDICINES -> BUTTON_5
            TAG_DIET -> BUTTON_6
            else -> BUTTON_1
        }
    }

}