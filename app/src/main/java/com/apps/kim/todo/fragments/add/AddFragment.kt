package com.apps.kim.todo.fragments.add

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
import com.apps.kim.todo.tools.extensions.*
import kotlinx.android.synthetic.main.fragment_add.*

/**
Created by KIM on 25.09.2019
 **/

class AddFragment : Fragment(), AddView, View.OnClickListener {

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
    private val presenter = AddPresenter(this)
    private var alarmManager: AlarmManager? = null
    private lateinit var myIntent: Intent
    private lateinit var timeLiveData1: MutableLiveData<String>
    private lateinit var timeLiveData2: MutableLiveData<String>
    private lateinit var timeLiveData3: MutableLiveData<String>
    private lateinit var timeLiveData4: MutableLiveData<String>
    private lateinit var timeLiveData5: MutableLiveData<String>
    private lateinit var timeLiveData6: MutableLiveData<String>
    lateinit var startDate: MutableLiveData<String>
    private var callback: AddCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        callback?.menuVisibility(true)
        imageSaveBtn.loadImage(R.drawable.clock)
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        myIntent = Intent(context, AlarmReceiver::class.java)
        switchAddSpecific.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) setSpecificDays(true)
            else setSpecificDays(false)
        }
        timeLiveData1 = DataController.instance.getTimeData1()
        timeLiveData1.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime1.text = resources.getString(R.string.pick_a_time)
            else textAddTime1.text = it
        })
        timeLiveData2 = DataController.instance.getTimeData2()
        timeLiveData2.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime2.text = resources.getString(R.string.pick_a_time)
            else textAddTime2.text = it
        })
        timeLiveData3 = DataController.instance.getTimeData3()
        timeLiveData3.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime3.text = resources.getString(R.string.pick_a_time)
            else textAddTime3.text = it
        })
        timeLiveData4 = DataController.instance.getTimeData4()
        timeLiveData4.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime4.text = resources.getString(R.string.pick_a_time)
            else textAddTime4.text = it
        })
        timeLiveData5 = DataController.instance.getTimeData5()
        timeLiveData5.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime5.text = resources.getString(R.string.pick_a_time)
            else textAddTime5.text = it
        })
        timeLiveData6 = DataController.instance.getTimeData6()
        timeLiveData6.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textAddTime6.text = resources.getString(R.string.pick_a_time)
            else textAddTime6.text = it
        })
        startDate = DataController.instance.getStartDate()
        startDate.observe(this, Observer<String> { textAddStart.text = it })

        spinnerAddTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hideKeyboard()
                presenter.showButtons(position)
            }
        }
        updateDays()
        setClickListeners(
            cardHomeTime,
            cardStartDay,
            buttonAddTime1,
            buttonAddTime2,
            buttonAddTime3,
            buttonAddTime4,
            buttonAddTime5,
            buttonAddTime6,
            imageAddTimeDelete1,
            imageAddTimeDelete2,
            imageAddTimeDelete3,
            imageAddTimeDelete4,
            imageAddTimeDelete5,
            imageAddTimeDelete6,
            cardAddSunday,
            cardAddMonday,
            cardAddTuesday,
            cardAddWednesday,
            cardAddThursday,
            cardAddFriday,
            cardAddSaturday
        )
    }

    override fun onClick(v: View?) {
        hideKeyboard()
        when (v) {
            cardStartDay -> callback?.showDatePickFragment()
            buttonAddTime1 -> openTimePicker(BUTTON_1)
            buttonAddTime2 -> openTimePicker(BUTTON_2)
            buttonAddTime3 -> openTimePicker(BUTTON_3)
            buttonAddTime4 -> openTimePicker(BUTTON_4)
            buttonAddTime5 -> openTimePicker(BUTTON_5)
            buttonAddTime6 -> openTimePicker(BUTTON_6)
            imageAddTimeDelete1 -> deleteReminder(BUTTON_1)
            imageAddTimeDelete2 -> deleteReminder(BUTTON_2)
            imageAddTimeDelete3 -> deleteReminder(BUTTON_3)
            imageAddTimeDelete4 -> deleteReminder(BUTTON_4)
            imageAddTimeDelete5 -> deleteReminder(BUTTON_5)
            imageAddTimeDelete6 -> deleteReminder(BUTTON_6)
            cardHomeTime -> presenter.saveReminder()
            cardAddSunday -> setSunday(isSun)
            cardAddMonday -> setMonday(isMon)
            cardAddTuesday -> setTuesday(isTue)
            cardAddWednesday -> setWednesday(isWed)
            cardAddThursday -> setThursday(isThu)
            cardAddFriday -> setFriday(isFri)
            cardAddSaturday -> setSaturday(isSat)
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

    override fun getTitle(): String = editTitle.text.toString()
    override fun setSpinnerPosition(position: Int) = spinnerAddTime.setSelection(position)
    override fun getSpinnerPosition(): Int = spinnerAddTime.selectedItemPosition
    override fun setSpinnerIntervalPosition(position: Int) = spinnerTimePickerDays.setSelection(position)
    override fun getSpinnerIntervalPosition(): Int = spinnerTimePickerDays.selectedItemPosition
    override fun clearTitle() = editTitle.setText(EMPTY_STRING)
    override fun getTextButton1(): String = textAddTime1.text.toString()
    override fun getTextButton2(): String = textAddTime2.text.toString()
    override fun getTextButton3(): String = textAddTime3.text.toString()
    override fun getTextButton4(): String = textAddTime4.text.toString()
    override fun getTextButton5(): String = textAddTime5.text.toString()
    override fun getTextButton6(): String = textAddTime6.text.toString()
    override fun showButton2() = constraintAddTime2.show()
    override fun showButton3() = constraintAddTime3.show()
    override fun showButton4() = constraintAddTime4.show()
    override fun showButton5() = constraintAddTime5.show()
    override fun showButton6() = constraintAddTime6.show()
    override fun hideButton2() = constraintAddTime2.hide(true)
    override fun hideButton3() = constraintAddTime3.hide(true)
    override fun hideButton4() = constraintAddTime4.hide(true)
    override fun hideButton5() = constraintAddTime5.hide(true)
    override fun hideButton6() = constraintAddTime6.hide(true)
    override fun getAlarmManager(): AlarmManager? = alarmManager
    override fun getStartDate(): String = textAddStart.text.toString()

    override fun getServiceIntent(pendingId: Int): Intent =
        myIntent.putExtra(DATA_PENDING_ID, pendingId)

    override fun getPendingIntent(id: Int, intent: Intent): PendingIntent =
        PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    override fun setSpecificDays(isActive: Boolean) {
        if (isActive) {
            cardAddSpecificDays.show()
            spinnerTimePickerDays.hide(true)
        } else {
            cardAddSpecificDays.hide(true)
            spinnerTimePickerDays.show()
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
            cardAddSunday.setBackgroundColor(Color.BLACK)
            textAddSunday.setTextColor(Color.WHITE)
        } else {
            isSun = true
            mSunday = null
            cardAddSunday.setBackgroundColor(Color.WHITE)
            textAddSunday.setTextColor(Color.BLACK)
        }
    }

    override fun setMonday(isActive: Boolean) {
        if (isActive) {
            isMon = false
            mMonday = 2
            cardAddMonday.setBackgroundColor(Color.BLACK)
            textAddMonday.setTextColor(Color.WHITE)
        } else {
            isMon = true
            mMonday = null
            cardAddMonday.setBackgroundColor(Color.WHITE)
            textAddMonday.setTextColor(Color.BLACK)
        }
    }

    override fun setTuesday(isActive: Boolean) {
        if (isActive) {
            isTue = false
            mTuesday = 3
            cardAddTuesday.setBackgroundColor(Color.BLACK)
            textAddTuesday.setTextColor(Color.WHITE)
        } else {
            isTue = true
            mTuesday = null
            cardAddTuesday.setBackgroundColor(Color.WHITE)
            textAddTuesday.setTextColor(Color.BLACK)
        }
    }

    override fun setWednesday(isActive: Boolean) {
        if (isActive) {
            isWed = false
            mWednesday = 4
            cardAddWednesday.setBackgroundColor(Color.BLACK)
            textAddWednesday.setTextColor(Color.WHITE)
        } else {
            isWed = true
            mWednesday = null
            cardAddWednesday.setBackgroundColor(Color.WHITE)
            textAddWednesday.setTextColor(Color.BLACK)
        }
    }

    override fun setThursday(isActive: Boolean) {
        if (isActive) {
            isThu = false
            mThursday = 5
            cardAddThursday.setBackgroundColor(Color.BLACK)
            textAddThursday.setTextColor(Color.WHITE)
        } else {
            isThu = true
            mThursday = null
            cardAddThursday.setBackgroundColor(Color.WHITE)
            textAddThursday.setTextColor(Color.BLACK)
        }
    }

    override fun setFriday(isActive: Boolean) {
        if (isActive) {
            isFri = false
            mFriday = 6
            cardAddFriday.setBackgroundColor(Color.BLACK)
            textAddFriday.setTextColor(Color.WHITE)
        } else {
            isFri = true
            mFriday = null
            cardAddFriday.setBackgroundColor(Color.WHITE)
            textAddFriday.setTextColor(Color.BLACK)
        }
    }

    override fun setSaturday(isActive: Boolean) {
        if (isActive) {
            isSat = false
            mSaturday = 7
            cardAddSaturday.setBackgroundColor(Color.BLACK)
            textAddSaturday.setTextColor(Color.WHITE)
        } else {
            isSat = true
            mSaturday = null
            cardAddSaturday.setBackgroundColor(Color.WHITE)
            textAddSaturday.setTextColor(Color.BLACK)
        }
    }

    override fun isSpecificDays(): Boolean = switchAddSpecific.isChecked
    override fun getSunday(): Int? = mSunday
    override fun getMonday(): Int? = mMonday
    override fun getTuesday(): Int? = mTuesday
    override fun getWednesday(): Int? = mWednesday
    override fun getThursday(): Int? = mThursday
    override fun getFriday(): Int? = mFriday
    override fun getSaturday(): Int? = mSaturday
    override fun startAnim() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha)
        rootAddFragment.startAnimation(animation)
    }

    override fun specificOff() {
        switchAddSpecific.isChecked = false
    }

    override fun clearLiveData() {
        mSunday = null
        mMonday = null
        mTuesday = null
        mWednesday = null
        mThursday = null
        mFriday = null
        mSaturday = null
        isSun = true
        isMon = true
        isTue = true
        isWed = true
        isThu = true
        isFri = true
        isSat = true
        timeLiveData1.value = EMPTY_STRING
        timeLiveData2.value = EMPTY_STRING
        timeLiveData3.value = EMPTY_STRING
        timeLiveData4.value = EMPTY_STRING
        timeLiveData5.value = EMPTY_STRING
        timeLiveData6.value = EMPTY_STRING
        startDate.value = resources.getString(R.string.today)
    }

    override fun updateDays() {
        setSunday(!isSun)
        setMonday(!isMon)
        setTuesday(!isTue)
        setWednesday(!isWed)
        setThursday(!isThu)
        setFriday(!isFri)
        setSaturday(!isSat)
    }

    override fun onDestroy() {
        clearLiveData()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        if (context is AddCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}