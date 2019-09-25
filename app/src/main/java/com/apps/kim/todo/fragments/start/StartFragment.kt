package com.apps.kim.todo.fragments.start

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.extensions.loadImage
import com.apps.kim.todo.tools.extensions.setClickListeners
import com.apps.kim.todo.tools.utils.PrefProvider
import kotlinx.android.synthetic.main.fragment_start.*

/**
Created by KIM on 25.09.2019
 **/

class StartFragment : Fragment(), StartView, View.OnClickListener {

    private val presenter = StartPresenter(this)
    private var callback: StartCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        callback?.menuVisibility(false)
        presenter.init()
        setClickListeners(
            cardEditStart1,
            cardEditStart2,
            cardEditStart3,
            cardEditStart4,
            cardEditStart5,
            cardEditStart6
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            cardEditStart1 -> {
                PrefProvider.mTag = TAG_ALARM
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_1))
                callback?.menuVisibility(true)
            }
            cardEditStart2 -> {
                PrefProvider.mTag = TAG_FAMILY
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_2))
                callback?.menuVisibility(true)
            }
            cardEditStart3 -> {
                PrefProvider.mTag = TAG_FRIENDS
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_3))
                callback?.menuVisibility(true)
            }
            cardEditStart4 -> {
                PrefProvider.mTag =TAG_WORKOUT
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_4))
                callback?.menuVisibility(true)
            }
            cardEditStart5 -> {
                PrefProvider.mTag = TAG_MEDICINES
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_5))
                callback?.menuVisibility(true)
            }
            cardEditStart6 -> {
                PrefProvider.mTag =TAG_DIET
                callback?.showHomeFragment()
                callback?.changeBackground(presenter.getPath(BUTTON_6))
                callback?.menuVisibility(true)
            }
        }
    }

    override fun onAttach(context: Context) {
        if (context is StartCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    override fun setBtnImg1(path: String) = imageStart1.loadImage(path)
    override fun setBtnImg2(path: String) = imageStart2.loadImage(path)
    override fun setBtnImg3(path: String) = imageStart3.loadImage(path)
    override fun setBtnImg4(path: String) = imageStart4.loadImage(path)
    override fun setBtnImg5(path: String) = imageStart5.loadImage(path)
    override fun setBtnImg6(path: String) = imageStart6.loadImage(path)
    override fun setBtnText1(text: String) = textStart1.setText(text)
    override fun setBtnText2(text: String) = textStart2.setText(text)
    override fun setBtnText3(text: String) = textStart3.setText(text)
    override fun setBtnText4(text: String) = textStart4.setText(text)
    override fun setBtnText5(text: String) = textStart5.setText(text)
    override fun setBtnText6(text: String) = textStart6.setText(text)

}