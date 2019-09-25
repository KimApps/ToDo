package com.apps.kim.todo.fragments.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.kim.todo.R
import com.apps.kim.todo.app.App
import com.apps.kim.todo.db.TodoDB
import com.apps.kim.todo.db.TodoDB_
import com.apps.kim.todo.tools.extensions.hide
import com.apps.kim.todo.tools.extensions.show
import kotlinx.android.synthetic.main.item_task_list.view.*

/**
Created by KIM on 25.09.2019
 **/

interface CallbackTodoAdapter {
    fun showMedicine(medicineName: String)
    fun editMedicine(medicineName: String)
    fun deleteStop(medicineName: String)
    fun deletePlay(medicineName: String)
    fun deleteMedicine(medicineName: String)
}

class TodoListAdapter(
    private val callback: CallbackTodoAdapter,
    private val reminderTitleList: List<String>
) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private val reminderBox = App.boxStore.boxFor(TodoDB::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_task_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = reminderTitleList[position]
        val reminder = reminderBox.query()
            .equal(TodoDB_.title, title)
            .build().findFirst()
        if (reminder?.isPause == true) {
            holder.cardMedicineStop.hide(true)
            holder.cardMedicinePlay.show()
        } else {
            holder.cardMedicinePlay.hide(true)
            holder.cardMedicineStop.show()
        }
        holder.textMedicineName.text = title
        holder.textMedicineName.isSelected = true
        holder.cardMedicineView.setOnClickListener { callback.showMedicine(title) }
        holder.cardMedicineEdit.setOnClickListener { callback.editMedicine(title) }
        holder.cardMedicineDelete.setOnClickListener { callback.deleteMedicine(title) }
        holder.cardMedicineStop.setOnClickListener {
            callback.deleteStop(title)
            holder.cardMedicineStop.hide(true)
            holder.cardMedicinePlay.show()
        }
        holder.cardMedicinePlay.setOnClickListener {
            callback.deletePlay(title)
            holder.cardMedicinePlay.hide(true)
            holder.cardMedicineStop.show()
        }
    }

    override fun getItemCount(): Int = reminderTitleList.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textMedicineName = view.textMedicineName!!
        val cardMedicineView = view.cardMedicineView!!
        val cardMedicineEdit = view.cardMedicineEdit!!
        val cardMedicineStop = view.cardMedicineStop!!
        val cardMedicineDelete = view.cardMedicineDelete!!
        val cardMedicinePlay = view.cardMedicinePlay!!
    }
}