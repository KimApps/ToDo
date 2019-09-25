package com.apps.kim.todo.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.HistoryDb
import com.apps.kim.todo.db.HistoryDb_
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.extensions.hide
import com.apps.kim.todo.tools.extensions.show
import io.objectbox.relation.ToMany
import kotlinx.android.synthetic.main.item_day_reminder.view.*
import java.util.*

/**
Created by KIM on 25.09.2019
 **/

interface CallbackHomeAdapter {
    fun showList(title: String)
}

class HomeAdapter(
    private val callback: CallbackHomeAdapter,
    private val reminderList: MutableList<TodoDB>,
    private val date: Calendar
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)
    private val historyBox = App.boxStore.boxFor(HistoryDb::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_day_reminder, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val reminder = reminderList[position]
        val taken = "${reminder.title} ${reminder.timeHour} ${reminder.timeMinute} ${date.timeInMillis}"
        val minute = if (reminder.timeMinute!! < 10) "0${reminder.timeMinute}"
        else reminder.timeMinute.toString()
        holder.textHomeTime.text = "${reminder.timeHour}:$minute"
        holder.textHomeTitle.text = reminder.title
        holder.textHomeTitle.isSelected = true
        holder.cardHomeTime.setOnClickListener {
            callback.showList(reminder.title ?: EMPTY_STRING)
        }
        holder.switchMedicineTaken.isChecked = checkInHistory(reminder.history, taken)
        holder.switchMedicineTaken.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) reminder.history.add(HistoryDb("${reminder.title}", taken))
            else {
                val query = historyBox.query()
                    .equal(HistoryDb_.done, taken)
                    .build()
                query.remove()
            }
            reminderBox.put(reminder)
        }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        if (calendar >= date) holder.switchMedicineTaken.show()
        else holder.switchMedicineTaken.hide(false)
    }

    override fun getItemCount(): Int = reminderList.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textHomeTitle = view.textHomeTitle!!
        val textHomeTime = view.textHomeTime!!
        val switchMedicineTaken = view.switchMedicineTaken!!
        val imageClock = view.imageClock!!
        val cardHomeTime = view.cardHomeTime!!
    }

    private fun checkInHistory(historyList: ToMany<HistoryDb>, done: String): Boolean {
        for (i in historyList) if (i.done == done) return true
        return false
    }
}