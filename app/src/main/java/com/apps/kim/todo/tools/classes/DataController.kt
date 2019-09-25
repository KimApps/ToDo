package com.apps.kim.todo.tools.classes

import androidx.lifecycle.MutableLiveData

/**
Created by KIM on 25.09.2019
 **/

class DataController private constructor() {

    private object Holder {
        val INSTANCE = DataController()
    }

    companion object {
        val instance: DataController by lazy { Holder.INSTANCE }
    }

    private var timeData1 = MutableLiveData<String>()
    private var timeData2 = MutableLiveData<String>()
    private var timeData3 = MutableLiveData<String>()
    private var timeData4 = MutableLiveData<String>()
    private var timeData5 = MutableLiveData<String>()
    private var timeData6 = MutableLiveData<String>()
    private var startDate = MutableLiveData<String>()
    private var quote = MutableLiveData<String>()

    fun getTimeData1(): MutableLiveData<String> = timeData1
    fun getTimeData2(): MutableLiveData<String> = timeData2
    fun getTimeData3(): MutableLiveData<String> = timeData3
    fun getTimeData4(): MutableLiveData<String> = timeData4
    fun getTimeData5(): MutableLiveData<String> = timeData5
    fun getTimeData6(): MutableLiveData<String> = timeData6
    fun getStartDate(): MutableLiveData<String> = startDate
    fun getQuote(): MutableLiveData<String> = quote


}