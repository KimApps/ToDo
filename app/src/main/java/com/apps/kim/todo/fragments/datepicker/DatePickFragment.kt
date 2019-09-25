package com.apps.kim.todo.fragments.datepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.DataController
import com.apps.kim.todo.tools.extensions.setClickListeners
import kotlinx.android.synthetic.main.fragment_datepicker.*
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

class DatePickFragment : Fragment(), View.OnClickListener {

    lateinit var startDate: MutableLiveData<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_datepicker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        startDate = DataController.instance.getStartDate()

        setClickListeners(setStartDate, cancelStartDate)
    }

    override fun onClick(v: View?) {
        when (v) {
            setStartDate -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, datePicker.year)
                calendar.set(Calendar.MONTH, datePicker.month)
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
                if (calendar < Calendar.getInstance()) Toast.makeText(
                    context, getString(R.string.future_date), Toast.LENGTH_LONG
                ).show()
                else {
                    if (calendar == Calendar.getInstance()) startDate.value = getString(R.string.today)
                    else startDate.value = "${datePicker.year}.${datePicker.month + 1}.${getDay(datePicker.dayOfMonth)}"
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
            cancelStartDate -> activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun getDay(day: Int): String {
        return if (day < 10) "0$day"
        else day.toString()
    }

}