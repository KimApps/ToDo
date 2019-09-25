package com.apps.kim.todo.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.BUNDLE_LIST
import com.apps.kim.todo.tools.classes.DataController
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.extensions.setClickListeners
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

/**
Created by KIM on 25.09.2019
 **/
class HomeFragment : Fragment(), View.OnClickListener, HomeView, CallbackHomeAdapter {

    lateinit var horizontalCalendar: HorizontalCalendar
    private val presenter = HomePresenter(this)
    private var adapter: HomeAdapter? = null
    private var callback: HomeCallback? = null
    private lateinit var quote: MutableLiveData<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        /* start before 1 month from now */
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        /* end after 1 month from now */
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        horizontalCalendar = HorizontalCalendar.Builder(rootView, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .configure()
            .formatTopText("MMM")
            .formatMiddleText("dd")
            .formatBottomText("EEE")
            .textSize(14f, 24f, 14f)
            .showTopText(true)
            .showBottomText(true)
            .textColor(Color.LTGRAY, Color.WHITE)
            .end()
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                initRecycler(date)
            }
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        initRecycler(calendar)
        callback?.menuVisibility(true)
        quote = DataController.instance.getQuote()
        quote.observe(this, Observer<String> {
            if (it.isNullOrEmpty()) textHomeQuotes.text = EMPTY_STRING
            else textHomeQuotes.text = it
        })
        textHomeQuotes.isSelected = true
        setClickListeners(buttonToday)
    }

    override fun onClick(v: View?) {
        when (v) {
            buttonToday -> horizontalCalendar.goToday(false)
        }
    }

    fun initRecycler(date: Calendar) {
        recyclerHome.apply {
            adapter = HomeAdapter(this@HomeFragment, presenter.initList(date), date)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    override fun showList(title: String) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_LIST, title)
        callback?.showDetailsFragment(bundle)
    }

    override fun setQuote(quote: String?) {
        if (quote.isNullOrEmpty()) this.quote.value = EMPTY_STRING
        else this.quote.value = quote
    }

    override fun onAttach(context: Context) {
        if (context is HomeCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}