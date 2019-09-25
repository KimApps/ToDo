package com.apps.kim.todo.fragments.edit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.apps.kim.todo.R
import com.apps.kim.todo.alarm.AlarmReceiver
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.extensions.hide
import com.apps.kim.todo.tools.extensions.hideKeyboard
import com.apps.kim.todo.tools.extensions.setClickListeners
import com.apps.kim.todo.tools.extensions.show
import kotlinx.android.synthetic.main.fragment_edit.*

/**
Created by KIM on 25.09.2019
 **/

class EditFragment : Fragment(), EditView, View.OnClickListener {

    private var callback: EditCallback? = null
    private var mSunday: Int? = null
    private var mMonday: Int? = null
    private var mTuesday: Int? = null
    private var mWednesday: Int? = null
    private var mThursday: Int? = null
    private var mFriday: Int? = null
    private var mSaturday: Int? = null
    private var isSun: Boolean = true
    private var isMon: Boolean = true
    private var isTue: Boolean = true
    private var isWed: Boolean = true
    private var isThu: Boolean = true
    private var isFri: Boolean = true
    private var isSat: Boolean = true
    private val presenter = EditPresenter(this)
    private var alarmManager: AlarmManager? = null
    private lateinit var myIntent: Intent
    private lateinit var timeLiveData1: MutableLiveData<String>
    private lateinit var timeLiveData2: MutableLiveData<String>
    private lateinit var timeLiveData3: MutableLiveData<String>
    private lateinit var timeLiveData4: MutableLiveData<String>
    private lateinit var timeLiveData5: MutableLiveData<String>
    private lateinit var timeLiveData6: MutableLiveData<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        val title = arguments?.getString(BUNDLE_LIST) ?: ""
        presenter.initView(title)
    }

    private fun initView() {
        switchEditSpecific.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) setSpecificDays(true)
            else setSpecificDays(false)
        }
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        myIntent = Intent(context, AlarmReceiver::class.java)
        timeLiveData1 = DataController.instance.getTimeData1()
        timeLiveData1.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime1.text = resources.getString(R.string.pick_a_time)
            else textEditTime1.text = it
        })
        timeLiveData2 = DataController.instance.getTimeData2()
        timeLiveData2.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime2.text = resources.getString(R.string.pick_a_time)
            else textEditTime2.text = it
        })
        timeLiveData3 = DataController.instance.getTimeData3()
        timeLiveData3.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime3.text = resources.getString(R.string.pick_a_time)
            else textEditTime3.text = it
        })
        timeLiveData4 = DataController.instance.getTimeData4()
        timeLiveData4.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime4.text = resources.getString(R.string.pick_a_time)
            else textEditTime4.text = it
        })
        timeLiveData5 = DataController.instance.getTimeData5()
        timeLiveData5.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime5.text = resources.getString(R.string.pick_a_time)
            else textEditTime5.text = it
        })
        timeLiveData6 = DataController.instance.getTimeData6()
        timeLiveData6.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textEditTime6.text = resources.getString(R.string.pick_a_time)
            else textEditTime6.text = it
        })

        spinnerEditTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hideKeyboard()
                presenter.showButtons(position)
            }
        }
        setClickListeners(
            btnEditEdit1, btnEditEdit2, btnEditEdit3,
            btnEditEdit4, btnEditEdit5, btnEditEdit6,
            btnEditDelete1, btnEditDelete2, btnEditDelete3,
            btnEditDelete4, btnEditDelete5, btnEditDelete6,
            cardEditSave, cardEditSunday, cardEditMonday,
            cardEditTuesday, cardEditWednesday, cardEditThursday,
            cardEditFriday, cardEditSaturday, textEditTime1, textEditTime2,
            textEditTime3, textEditTime4, textEditTime5, textEditTime6
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            btnEditEdit1 -> openTimePicker(BUTTON_1)
            btnEditEdit2 -> openTimePicker(BUTTON_2)
            btnEditEdit3 -> openTimePicker(BUTTON_3)
            btnEditEdit4 -> openTimePicker(BUTTON_4)
            btnEditEdit5 -> openTimePicker(BUTTON_5)
            btnEditEdit6 -> openTimePicker(BUTTON_6)
            textEditTime1 -> openTimePicker(BUTTON_1)
            textEditTime2 -> openTimePicker(BUTTON_2)
            textEditTime3 -> openTimePicker(BUTTON_3)
            textEditTime4 -> openTimePicker(BUTTON_4)
            textEditTime5 -> openTimePicker(BUTTON_5)
            textEditTime6 -> openTimePicker(BUTTON_6)
            btnEditDelete1 -> deleteReminder(BUTTON_1)
            btnEditDelete2 -> deleteReminder(BUTTON_2)
            btnEditDelete3 -> deleteReminder(BUTTON_3)
            btnEditDelete4 -> deleteReminder(BUTTON_4)
            btnEditDelete5 -> deleteReminder(BUTTON_5)
            btnEditDelete6 -> deleteReminder(BUTTON_6)
            cardEditSave -> presenter.saveReminder()
            cardEditSunday -> setSunday(isSun)
            cardEditMonday -> setMonday(isMon)
            cardEditTuesday -> setTuesday(isTue)
            cardEditWednesday -> setWednesday(isWed)
            cardEditThursday -> setThursday(isThu)
            cardEditFriday -> setFriday(isFri)
            cardEditSaturday -> setSaturday(isSat)
        }
    }

    private fun openTimePicker(buttonNumber: String) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_BUTTON, buttonNumber)
        callback?.showTimePickFragment(bundle)
    }

    private fun deleteReminder(buttonNumber: String) {
        when (buttonNumber) {
            BUTTON_1 -> timeLiveData1.value = EMPTY_STRING
            BUTTON_2 -> timeLiveData2.value = EMPTY_STRING
            BUTTON_3 -> timeLiveData3.value = EMPTY_STRING
            BUTTON_4 -> timeLiveData4.value = EMPTY_STRING
            BUTTON_5 -> timeLiveData5.value = EMPTY_STRING
            BUTTON_6 -> timeLiveData6.value = EMPTY_STRING
        }
    }

    override fun setLiveData1(text: String) {
        timeLiveData1.value = text
    }

    override fun setLiveData2(text: String) {
        timeLiveData2.value = text
    }

    override fun setLiveData3(text: String) {
        timeLiveData3.value = text
    }

    override fun setLiveData4(text: String) {
        timeLiveData4.value = text
    }

    override fun setLiveData5(text: String) {
        timeLiveData5.value = text
    }

    override fun setLiveData6(text: String) {
        timeLiveData6.value = text
    }

    override fun clearLiveData() {
        timeLiveData1.value = EMPTY_STRING
        timeLiveData2.value = EMPTY_STRING
        timeLiveData3.value = EMPTY_STRING
        timeLiveData4.value = EMPTY_STRING
        timeLiveData5.value = EMPTY_STRING
        timeLiveData6.value = EMPTY_STRING
    }

    override fun setTitle(title: String) = editEditTitle.setText(title)
    override fun setInstruction(instruction: String) = editInstruction.setText(instruction)
    override fun getTitle(): String = editEditTitle.text.toString()
    override fun setSpinnerPosition(position: Int) = spinnerEditTime.setSelection(position)
    override fun getSpinnerPosition(): Int = spinnerEditTime.selectedItemPosition
    override fun setSpinnerIntervalPosition(position: Int) = spinnerEditDays.setSelection(position)
    override fun getSpinnerIntervalPosition(): Int = spinnerEditDays.selectedItemPosition
    override fun getTextButton1(): String = textEditTime1.text.toString()
    override fun getTextButton2(): String = textEditTime2.text.toString()
    override fun getTextButton3(): String = textEditTime3.text.toString()
    override fun getTextButton4(): String = textEditTime4.text.toString()
    override fun getTextButton5(): String = textEditTime5.text.toString()
    override fun getTextButton6(): String = textEditTime6.text.toString()
    override fun getLiveData1(): String? = timeLiveData1.value
    override fun getLiveData2(): String? = timeLiveData2.value
    override fun getLiveData3(): String? = timeLiveData3.value
    override fun getLiveData4(): String? = timeLiveData4.value
    override fun getLiveData5(): String? = timeLiveData5.value
    override fun getLiveData6(): String? = timeLiveData6.value
    override fun showButton2() = constEditTime2.show()
    override fun showButton3() = constEditTime3.show()
    override fun showButton4() = constEditTime4.show()
    override fun showButton5() = constEditTime5.show()
    override fun showButton6() = constEditTime6.show()
    override fun hideButton2() = constEditTime2.hide(true)
    override fun hideButton3() = constEditTime3.hide(true)
    override fun hideButton4() = constEditTime4.hide(true)
    override fun hideButton5() = constEditTime5.hide(true)
    override fun hideButton6() = constEditTime6.hide(true)
    override fun getInstruction(): String = editInstruction.text.toString()
    override fun getAlarmManager(): AlarmManager? = alarmManager
    override fun getServiceIntent(pendingId: Int): Intent =
        myIntent.putExtra(DATA_PENDING_ID, pendingId)

    override fun getPendingIntent(id: Int, intent: Intent): PendingIntent =
        PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    override fun startAnim() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha)
        rootEditFragment.startAnimation(animation)
    }

    override fun goBack() {
        fragmentManager?.popBackStack()
    }

    override fun setSpecificDays(isActive: Boolean) {
        if (isActive) {
            cardEditSpecificDays.show()
            spinnerEditDays.hide(true)
            switchEditSpecific.isChecked = true
        } else {
            cardEditSpecificDays.hide(true)
            spinnerEditDays.show()
            switchEditSpecific.isChecked = false
            isSun = true
            isMon = true
            isTue = true
            isWed = true
            isThu = true
            isFri = true
            isSat = true
            updateDays()
        }
    }

    override fun setSunday(isActive: Boolean) {
        if (isActive) {
            isSun = false
            mSunday = 1
            cardEditSunday.setBackgroundColor(Color.BLACK)
            textEditSunday.setTextColor(Color.WHITE)
        } else {
            isSun = true
            mSunday = null
            cardEditSunday.setBackgroundColor(Color.WHITE)
            textEditSunday.setTextColor(Color.BLACK)
        }
    }

    override fun setMonday(isActive: Boolean) {
        if (isActive) {
            isMon = false
            mMonday = 2
            cardEditMonday.setBackgroundColor(Color.BLACK)
            textEditMonday.setTextColor(Color.WHITE)
        } else {
            isMon = true
            mMonday = null
            cardEditMonday.setBackgroundColor(Color.WHITE)
            textEditMonday.setTextColor(Color.BLACK)
        }
    }

    override fun setTuesday(isActive: Boolean) {
        if (isActive) {
            isTue = false
            mTuesday = 3
            cardEditTuesday.setBackgroundColor(Color.BLACK)
            textEditTuesday.setTextColor(Color.WHITE)
        } else {
            isTue = true
            mTuesday = null
            cardEditTuesday.setBackgroundColor(Color.WHITE)
            textEditTuesday.setTextColor(Color.BLACK)
        }
    }

    override fun setWednesday(isActive: Boolean) {
        if (isActive) {
            isWed = false
            mWednesday = 4
            cardEditWednesday.setBackgroundColor(Color.BLACK)
            textEditWednesday.setTextColor(Color.WHITE)
        } else {
            isWed = true
            mWednesday = null
            cardEditWednesday.setBackgroundColor(Color.WHITE)
            textEditWednesday.setTextColor(Color.BLACK)
        }
    }

    override fun setThursday(isActive: Boolean) {
        if (isActive) {
            isThu = false
            mThursday = 5
            cardEditThursday.setBackgroundColor(Color.BLACK)
            textEditThursday.setTextColor(Color.WHITE)
        } else {
            isThu = true
            mThursday = null
            cardEditThursday.setBackgroundColor(Color.WHITE)
            textEditThursday.setTextColor(Color.BLACK)
        }
    }

    override fun setFriday(isActive: Boolean) {
        if (isActive) {
            isFri = false
            mFriday = 6
            cardEditFriday.setBackgroundColor(Color.BLACK)
            textEditFriday.setTextColor(Color.WHITE)
        } else {
            isFri = true
            mFriday = null
            cardEditFriday.setBackgroundColor(Color.WHITE)
            textEditFriday.setTextColor(Color.BLACK)
        }
    }

    override fun setSaturday(isActive: Boolean) {
        if (isActive) {
            isSat = false
            mSaturday = 7
            cardEditSaturday.setBackgroundColor(Color.BLACK)
            textEditSaturday.setTextColor(Color.WHITE)
        } else {
            isSat = true
            mSaturday = null
            cardEditSaturday.setBackgroundColor(Color.WHITE)
            textEditSaturday.setTextColor(Color.BLACK)
        }
    }

    override fun isSpecificDays(): Boolean = switchEditSpecific.isChecked
    override fun getSunday(): Int? = mSunday
    override fun getMonday(): Int? = mMonday
    override fun getTuesday(): Int? = mTuesday
    override fun getWednesday(): Int? = mWednesday
    override fun getThursday(): Int? = mThursday
    override fun getFriday(): Int? = mFriday
    override fun getSaturday(): Int? = mSaturday

    override fun updateDays() {
        setSunday(!isSun)
        setMonday(!isMon)
        setTuesday(!isTue)
        setWednesday(!isWed)
        setThursday(!isThu)
        setFriday(!isFri)
        setSaturday(!isSat)
    }

    override fun specificOff() {
        switchEditSpecific.isChecked = false
    }

    override fun onDestroy() {
        clearLiveData()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        if (context is EditCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}