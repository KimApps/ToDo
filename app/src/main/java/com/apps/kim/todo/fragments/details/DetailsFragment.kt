package com.apps.kim.todo.fragments.details

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.classes.BUNDLE_LIST
import com.apps.kim.todo.tools.extensions.hide
import com.apps.kim.todo.tools.extensions.show
import kotlinx.android.synthetic.main.fragment_details.*

/**
Created by KIM on 25.09.2019
 **/

class DetailsFragment : Fragment(), DetailsView {

    //    private var callback: DetailsCallback? = null
    private val presenter = DetailsPresenter(this)
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)
    private lateinit var reminders: MutableList<TodoDB>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reminders = reminderBox.query()
            .equal(TodoDB_.title, arguments?.getString(BUNDLE_LIST) ?: "")
            .build()
            .find()
        presenter.initView(reminders)
    }

    override fun setTitle(title: String) {
        textDetailsTitle.text = title
    }

    override fun setPeriod(period: String) {
        textDetailsPeriod.text = period
    }

    override fun setTime1(time: String) {
        textDetailsTime1.text = time
    }

    override fun setTime2(time: String) {
        textDetailsTime2.text = time
    }

    override fun setTime3(time: String) {
        textDetailsTime3.text = time
    }

    override fun setTime4(time: String) {
        textDetailsTime4.text = time
    }

    override fun setTime5(time: String) {
        textDetailsTime5.text = time
    }

    override fun setTime6(time: String) {
        textDetailsTime6.text = time
    }

    override fun setInstruction(instruction: String) {
        textDetailsInstruction.text = instruction
    }

    override fun setStatus(status: String) {
        textDetailsStatus.text = status
    }

    override fun setStartDate(date: String) {
        textDetailsStartDate.text = date
    }

    override fun setTaken(taken: String) {
        textDetailsTaken.text = taken
    }

    override fun timeVisible2(isVisible: Boolean) {
        if (isVisible) constDetailsTime2.show()
        else constDetailsTime2.hide(true)
    }

    override fun timeVisible3(isVisible: Boolean) {
        if (isVisible) constDetailsTime3.show()
        else constDetailsTime3.hide(true)
    }

    override fun timeVisible4(isVisible: Boolean) {
        if (isVisible) constDetailsTime4.show()
        else constDetailsTime4.hide(true)
    }

    override fun timeVisible5(isVisible: Boolean) {
        if (isVisible) constDetailsTime5.show()
        else constDetailsTime5.hide(true)
    }

    override fun timeVisible6(isVisible: Boolean) {
        if (isVisible) constDetailsTime6.show()
        else constDetailsTime6.hide(true)
    }

    override fun setFinishDate(date: String) {
        //todo
    }

}