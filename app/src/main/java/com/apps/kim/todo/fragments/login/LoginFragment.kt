package com.apps.kim.todo.fragments.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.tools.extensions.setClickListeners
import kotlinx.android.synthetic.main.fragment_login.*


/**
Created by KIM on 26.09.2019
 **/

class LoginFragment : Fragment(), LoginView, View.OnClickListener {

    private val presenter = LoginPresenter(this)
    private var callback: LoginCallback? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        setClickListeners(btnFacebook, btnGoogle, textLogIn)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnFacebook -> callback?.fbAuth()
            btnGoogle -> callback?.googleAuth()
            textLogIn -> callback?.startMain()
        }
    }


    override fun onAttach(context: Context) {
        if (context is LoginCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}