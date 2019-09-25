package com.apps.kim.todo.fragments.customize

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.apps.kim.todo.R
import com.apps.kim.todo.photopicker.PickImageActivity
import com.apps.kim.todo.tools.classes.*
import com.apps.kim.todo.tools.extensions.loadImage
import com.apps.kim.todo.tools.extensions.setClickListeners
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.AudioPickActivity
import kotlinx.android.synthetic.main.fragment_customize.*

/**
Created by KIM on 25.09.2019
 **/

class CustomizeFragment : Fragment(), CustomizeView, View.OnClickListener {

    private val presenter = CustomizePresenter(this)
    private var callback: CustomizeCallback? = null
    private var buttonNumber = EMPTY_STRING

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.retainInstance = true
        return inflater.inflate(R.layout.fragment_customize, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        callback?.menuVisibility(true)
        buttonNumber = presenter.getTag()
        imageCustomizeSave.loadImage(R.drawable.clock)
        imageCustomizeLogo.loadImage(R.drawable.time_logo)
        presenter.init(buttonNumber)
        setClickListeners(
            imageCustomizeSave,
            cardCustomizeBackDefault, cardCustomizeBtnDefault, btnCustomizeDefaultSound,
            cardCustomizeBackGallery, cardCustomizeBtnGallery, btnCustomizePickAudio
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            imageCustomizeSave -> presenter.save(buttonNumber)
            cardCustomizeBackDefault -> presenter.setDefBackground(buttonNumber)
            cardCustomizeBtnDefault -> presenter.setDefImage(buttonNumber)
            cardCustomizeBackGallery -> pickFromGallery(getBackRequestCode(buttonNumber))
            cardCustomizeBtnGallery -> pickFromGallery(getBtnRequestCode(buttonNumber))
            btnCustomizeDefaultSound -> presenter.setDefaultAudio(buttonNumber)
            btnCustomizePickAudio -> {
                val intent = Intent(context, AudioPickActivity::class.java)
                intent.putExtra(Constant.MAX_NUMBER, 1)
                startActivityForResult(intent, Constant.REQUEST_CODE_PICK_AUDIO)
            }
        }
    }

    private fun pickFromGallery(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startPickImageActivity(requestCode)
        } else {
            requestPermissionGallery(requestCode)
        }
    }

    private fun requestPermissionGallery(requestCode: Int) {
        val request = permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build()
        request.send()
        request.listeners {
            onAccepted {
                startPickImageActivity(requestCode)
            }
            onDenied {
                // Notified when the permissions are denied.
            }
        }
    }

    private fun startPickImageActivity(requestCode: Int) {
        val mIntent = Intent(context, PickImageActivity::class.java)
        mIntent.putExtra(KEY_LIMIT_MAX_IMAGE, 1)
        mIntent.putExtra(KEY_LIMIT_MIN_IMAGE, 1)
        startActivityForResult(mIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_PICK_AUDIO) {
                presenter.setAudio(buttonNumber, intent)
            } else if (intent != null) {
                presenter.onGalleryImageResult(intent, requestCode)
            }
        } else return
        ///storage/388F-1824/Android/data/com.busuu.android.enc/files/learning_content/https/cdn.busuu.com/v1.0/mp3m2n/media/audio/placement_test_bucket_2__11_1505905168.mp3
    }

    private fun getBackRequestCode(button: String): Int {
        return when (button) {
            BUTTON_2 -> REQUEST_GALLERY_BACK2
            BUTTON_3 -> REQUEST_GALLERY_BACK3
            BUTTON_4 -> REQUEST_GALLERY_BACK4
            BUTTON_5 -> REQUEST_GALLERY_BACK5
            BUTTON_6 -> REQUEST_GALLERY_BACK6
            else -> REQUEST_GALLERY_BACK1
        }
    }

    private fun getBtnRequestCode(button: String): Int {
        return when (button) {
            BUTTON_2 -> REQUEST_GALLERY_IMG2
            BUTTON_3 -> REQUEST_GALLERY_IMG3
            BUTTON_4 -> REQUEST_GALLERY_IMG4
            BUTTON_5 -> REQUEST_GALLERY_IMG5
            BUTTON_6 -> REQUEST_GALLERY_IMG6
            else -> REQUEST_GALLERY_IMG1
        }
    }

    override fun setTextColorImageDefault(colorId: Int) = textCustomizeImage.setTextColor(colorId)
    override fun setTextColorBackDefault(colorId: Int) = textCustomizeBack.setTextColor(colorId)
    override fun setBtnColorImage(colorId: Int) = cardCustomizeBtnGallery.setCardBackgroundColor(colorId)
    override fun setBtnColorBack(colorId: Int) = cardCustomizeBackGallery.setCardBackgroundColor(colorId)
    override fun setBtnColorImageDefault(colorId: Int) = cardCustomizeBtnDefault.setCardBackgroundColor(colorId)
    override fun setBtnColorBackDefault(colorId: Int) = cardCustomizeBackDefault.setCardBackgroundColor(colorId)
    override fun setIconImage(path: String) = imageCustomizeBtnCamera.loadImage(path)
    override fun setIconBack(path: String) = imageCustomizeBackCamera.loadImage(path)
    override fun setBtnText(text: String) = editCustomizeButton.setText(text)
    override fun getBtnText(): String = editCustomizeButton.text.toString()
    override fun getStringResource(id: Int): String = resources.getString(id)
    override fun setAudioFileName(name: String) = textCustomizeFileName.setText(name)
    override fun goBack() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onAttach(context: Context) {
        if (context is CustomizeCallback) callback = context
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}