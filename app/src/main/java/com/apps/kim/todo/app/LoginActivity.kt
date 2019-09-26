package com.apps.kim.todo.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.fragments.login.LoginCallback
import com.apps.kim.todo.fragments.login.LoginFragment
import com.apps.kim.todo.tools.classes.DETAILS_FRAGMENT
import com.apps.kim.todo.tools.classes.EMPTY_STRING
import com.apps.kim.todo.tools.classes.LOGIN_FRAGMENT
import com.apps.kim.todo.tools.classes.UserFacebookModel
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener,
    LoginCallback {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var gso: GoogleSignInOptions
    private val RC_SIGNIN = 0
    private var callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var googleSignInAccount: GoogleSignInAccount? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addFragment(LoginFragment(), LOGIN_FRAGMENT)
        initFacebook()
        initOnGoogle()
    }

    private fun addFragment(fragment: Fragment, tagFragment: String) {
        if (getCurrentTag() == fragment.tag) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.mContainer, fragment, tagFragment)
            .addToBackStack(tagFragment)
            .commitAllowingStateLoss()
    }

    private fun fbLogInBtn() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "public_profile"))
    }

    private fun googleLogInBtn() {
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleSignInAccount != null) {
            val client: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            client.signOut()
            //googleLoginBtn.text = "google"
        } else {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGNIN)
        }
    }

    private fun registerUser(mName: String?, mSurname: String?, mEmail: String?, userId: String?) {
        App.instance.toast(mEmail ?: EMPTY_STRING)
        startMain()
        Log.d("initFacebook", "onSuccess")
    }

    //Google authorization...
    private fun initOnGoogle() {
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
//        if (googleSignInAccount != null) googleLoginBtn.text = "log out"
//        else googleLoginBtn.text = "google"
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGNIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val acct = result.signInAccount
                // Get account information
                val mFullName = acct!!.displayName
                val mName = acct.givenName
                val mSurname = acct.familyName
                val mEmail = acct.email
                val userId = acct.id
                registerUser(mName, mSurname, mEmail, userId)
                // textGoogleBtn.text = resources.getString(R.string.log_out)
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    //...Google authorization

    //Facebook authorization...

    private fun initFacebook() {

        fbLoginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Log.d("initFacebook", "onSuccess")
                facebookGetData()
            }

            override fun onCancel() {
                // App code
                LoginManager.getInstance().logOut()
                Log.d("initFacebook", "onCancel")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.d("initFacebook", "onError")
            }
        })
    }

    fun facebookGetData() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->
            // Application code
            Log.d("onCompleted", "onCompleted facebookGetData GraphRequest")
            if (`object` != null) {
                val builder = GsonBuilder()
                val gson = builder.create()
                val user = gson.fromJson<UserFacebookModel>(
                    `object`.toString(),
                    UserFacebookModel::class.java
                )
                Log.d("onCompleted", user.toString())
                registerUser(user.first_name, user.last_name, user.email, user.id)
                //textFacebookBtn.text = resources.getString(R.string.log_out)
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,first_name,last_name,email")
        request.parameters = parameters
        request.executeAsync()
    }
    //...Facebook authorization

    override fun googleAuth() = googleLogInBtn()
    override fun fbAuth() = fbLogInBtn()
    override fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getCurrentTag(): String {
        return if ((supportFragmentManager.findFragmentById(R.id.mContainer) != null)) {
            (supportFragmentManager.findFragmentById(R.id.mContainer))?.tag ?: ""
        } else ""
    }
}
