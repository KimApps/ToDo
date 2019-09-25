package com.apps.kim.todo.app

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.fragments.add.AddCallback
import com.apps.kim.todo.fragments.add.AddFragment
import com.apps.kim.todo.fragments.customize.CustomizeCallback
import com.apps.kim.todo.fragments.customize.CustomizeFragment
import com.apps.kim.todo.fragments.datepicker.DatePickFragment
import com.apps.kim.todo.fragments.details.DetailsFragment
import com.apps.kim.todo.fragments.edit.EditCallback
import com.apps.kim.todo.fragments.edit.EditFragment
import com.apps.kim.todo.fragments.home.HomeCallback
import com.apps.kim.todo.fragments.home.HomeFragment
import com.apps.kim.todo.fragments.start.StartCallback
import com.apps.kim.todo.fragments.start.StartFragment
import com.apps.kim.todo.fragments.timepicker.TimePickerFragment
import com.apps.kim.todo.fragments.todolist.TodoListCallback
import com.apps.kim.todo.fragments.todolist.TodoListFragment
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.extensions.hide
import com.apps.kim.todo.tools.extensions.loadImage
import com.apps.kim.todo.tools.extensions.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StartCallback, AddCallback, EditCallback,
    TodoListCallback, HomeCallback, CustomizeCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationBottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationBottomView.selectedItemId = R.id.startNavigation
        requestPermission()
    }

    override fun changeBackground(path: String) = mainBackground.loadImage(path)
    override fun menuVisibility(isVisible: Boolean) {
        if (isVisible) navigationBottomView.show()
        else navigationBottomView.hide(false)
    }

    override fun onBackPressed() {
        if (getCurrentTag() == START_FRAGMENT) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.close_app))
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(true)
                .setPositiveButton(
                    getString(R.string.ok)
                ) { dialog, id ->
                    dialog.cancel()
                    finish()
                }
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog, id ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
        } else super.onBackPressed()
    }

    private fun addFragment(fragment: Fragment, tagFragment: String, bundle: Bundle?) {
        if (getCurrentTag() == fragment.tag) return
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.mContainer, fragment, tagFragment)
            .addToBackStack(tagFragment)
            .commitAllowingStateLoss()
    }

    private fun clearStack(index: Int) {
        var count = supportFragmentManager.backStackEntryCount
        while (count > index) {
            supportFragmentManager.popBackStack()
            count--
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.startNavigation -> {
                clearStack(0)
                showStartFragment()
                true
            }
            R.id.homeNavigation -> {
                addFragment(HomeFragment(), HOME_FRAGMENT, null)
                true
            }
            R.id.listNavigation -> {
                showListFragment()
                true
            }
            R.id.addNavigation -> {
                showAddFragment()
                true
            }
            R.id.customizeNavigation -> {
                showCustomizeFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showStartFragment() = addFragment(StartFragment(), START_FRAGMENT, null)
    override fun showHomeFragment() {navigationBottomView.selectedItemId = R.id.homeNavigation}
    private fun showListFragment() = addFragment(TodoListFragment(), LIST_FRAGMENT, null)
    private fun showAddFragment() = addFragment(AddFragment(), ADD_FRAGMENT, null)
    private fun showCustomizeFragment() = addFragment(CustomizeFragment(), CUSTOMIZE_FRAGMENT, null)
    override fun showDatePickFragment() = addFragment(DatePickFragment(), DATE_FRAGMENT, null)
    override fun showTimePickFragment(bundle: Bundle) = addFragment(TimePickerFragment(), TIME_FRAGMENT, bundle)
    override fun showDetailsFragment(bundle: Bundle) = addFragment(DetailsFragment(), DETAILS_FRAGMENT, bundle)
    override fun showEditFragment(bundle: Bundle) = addFragment(EditFragment(), EDIT_FRAGMENT, bundle)

    private fun setMenuItem(tag: String) {
        when (tag) {
            START_FRAGMENT -> navigationBottomView.selectedItemId = R.id.startNavigation
            HOME_FRAGMENT -> navigationBottomView.selectedItemId = R.id.homeNavigation
            LIST_FRAGMENT -> navigationBottomView.selectedItemId = R.id.listNavigation
            ADD_FRAGMENT -> navigationBottomView.selectedItemId = R.id.addNavigation
            CUSTOMIZE_FRAGMENT -> navigationBottomView.selectedItemId = R.id.customizeNavigation
        }
    }

    private fun getCurrentTag(): String {
        return if ((supportFragmentManager.findFragmentById(R.id.mContainer) != null)) {
            (supportFragmentManager.findFragmentById(R.id.mContainer))?.tag ?: ""
        } else ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111) {

        }
    }

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 1111)
                return true
            }
        }
        return false
    }

}

