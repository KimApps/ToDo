package com.apps.kim.todo.fragments.timepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.extensions.setClickListeners
import kotlinx.android.synthetic.main.fragment_timepicker.*
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class TimePickerFragment : Fragment(), View.OnClickListener {

    private lateinit var timeLiveData1: MutableLiveData<String>
    private lateinit var timeLiveData2: MutableLiveData<String>
    private lateinit var timeLiveData3: MutableLiveData<String>
    private lateinit var timeLiveData4: MutableLiveData<String>
    private lateinit var timeLiveData5: MutableLiveData<String>
    private lateinit var timeLiveData6: MutableLiveData<String>
    private lateinit var calendar: Calendar
    private var buttonNumber = EMPTY_STRING

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_timepicker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        buttonNumber = arguments?.getString(BUNDLE_BUTTON) ?: EMPTY_STRING
        timeLiveData1 = DataController.instance.getTimeData1()
        timeLiveData2 = DataController.instance.getTimeData2()
        timeLiveData3 = DataController.instance.getTimeData3()
        timeLiveData4 = DataController.instance.getTimeData4()
        timeLiveData5 = DataController.instance.getTimeData5()
        timeLiveData6 = DataController.instance.getTimeData6()
        calendar = Calendar.getInstance()

        setClickListeners(setReminder, cancelReminder)
    }

    override fun onClick(v: View?) {
        when (v) {
            setReminder -> {

                val hour = datePicker.currentHour.toString()
                var minute = datePicker.currentMinute.toString()
                if (minute.length < 2) minute = "0$minute"
                when (buttonNumber) {
                    BUTTON_1 -> timeLiveData1.value = "$hour:$minute"
                    BUTTON_2 -> timeLiveData2.value = "$hour:$minute"
                    BUTTON_3 -> timeLiveData3.value = "$hour:$minute"
                    BUTTON_4 -> timeLiveData4.value = "$hour:$minute"
                    BUTTON_5 -> timeLiveData5.value = "$hour:$minute"
                    BUTTON_6 -> timeLiveData6.value = "$hour:$minute"
                }
                fragmentManager?.popBackStack()
            }
            cancelReminder -> fragmentManager?.popBackStack()
        }
    }
}