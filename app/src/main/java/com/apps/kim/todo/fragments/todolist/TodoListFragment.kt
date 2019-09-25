package com.apps.kim.todo.fragments.todolist

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.kim.todo.R
import com.apps.kim.todo.alarm.AlarmReceiver
import com.apps.kim.todo.tools.classes.BUNDLE_LIST
import com.apps.kim.todo.tools.classes.DATA_PENDING_ID
import kotlinx.android.synthetic.main.fragment_medicine.*

/**
Created by KIM on 25.09.2019
 **/

class TodoListFragment : Fragment(), TodoListView, CallbackTodoAdapter {

    private val presenter: TodoListPresenter = TodoListPresenter(this)
    private lateinit var myIntent: Intent
    private lateinit var alarmManager: AlarmManager
    private var callback: TodoListCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_medicine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        callback?.menuVisibility(true)
        myIntent = Intent(context, AlarmReceiver::class.java)
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val res: RecyclerView
        initRecycler()
    }

    private fun initRecycler() {
        recyclerMedicine.apply {
            adapter = TodoListAdapter(this@TodoListFragment, presenter.getReminderList())
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    override fun showMedicine(medicineName: String) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_LIST, medicineName)
        callback?.showDetailsFragment(bundle)
    }

    override fun editMedicine(medicineName: String) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_LIST, medicineName)
        callback?.showEditFragment(bundle)
    }

    override fun cancelAlarm(pendingId: Int) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, pendingId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    override fun deleteStop(medicineName: String) = presenter.stopDelete(medicineName, false)
    override fun deletePlay(medicineName: String) = presenter.startReminders(medicineName)
    override fun deleteMedicine(medicineName: String) = showAlertDialog(medicineName)
    override fun getAlarmManager(): AlarmManager? = alarmManager
    override fun getPendingIntent(id: Int, intent: Intent): PendingIntent =
        PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    override fun getServiceIntent(hour: Int, minute: Int, period: Int, pendingId: Long): Intent =
        myIntent.putExtra(DATA_PENDING_ID, pendingId)

    override fun showAlertDialog(medicineName: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.delete_message))
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, id ->
                dialog.cancel()
                presenter.stopDelete(medicineName, true)
                initRecycler()
            }
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, id ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onAttach(context: Context) {
        if (context is TodoListCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}