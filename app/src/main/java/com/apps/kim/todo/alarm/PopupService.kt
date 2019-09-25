package com.apps.kim.todo.alarm

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.apps.kim.todo.R

/**
Created by KIM on 25.09.2019
 **/

class PopupService : Service() {

    private var mView: View? = null
    private var mParams: WindowManager.LayoutParams? = null
    private var mWindowManager: WindowManager? = null

    override fun onCreate() {
        super.onCreate()
        mView = MyLoadView(this)
        mParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (mWindowManager != null) {
            mWindowManager?.addView(mView, mParams)
            Handler().postDelayed({
                stopSelf()
            }, 7000)
        }
    }

    override fun onBind(intent: Intent): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.removeView(mView)
        mView = null
    }

    inner class MyLoadView(context: Context) : View(context) {
        private val mPaint: Paint = Paint()
        private var xLogo = 0f
        private var yLogo = 30f
        private var xDir = 5f
        private var radius1 = 140f
        private var radius2 = 80f
        private var radiusDir1 = 1f
        private var radiusDir2 = 1f
        private var scX = 0f
        private var scY = 0f
        private var logoSizeX = 160f
        private var logoSizeY = 160f
        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val logo = BitmapFactory.decodeResource(resources, R.drawable.time_logo)
            mPaint.color = ContextCompat.getColor(context, R.color.blackAlpha20)
            mPaint.style = Paint.Style.FILL
            scX = logoSizeX / logo.width
            scY = logoSizeY / logo.height
            val addX = logo.width / 2 * scX
            val addY = logo.height / 2 * scY
            // val bitmapWidth = logo.width * scX
            // if (xLogo >= (width - bitmapWidth - 30)) xDir = -4f
            // if (xLogo <= 30) xDir = 4f
            if (radius1 >= 140) radiusDir1 = -1f
            if (radius1 <= 80) radiusDir1 = 1f
            if (radius2 >= 140) radiusDir2 = -1f
            if (radius2 <= 80) radiusDir2 = 1f
            radius1 += radiusDir1
            radius2 += radiusDir2
            xLogo += xDir
            canvas.drawCircle(xLogo + addX, yLogo + addY, radius1, mPaint)
            canvas.drawCircle(xLogo + addX, yLogo + addY, radius2, mPaint)
            val matrix = Matrix()
            matrix.postScale(scX, scY)
            matrix.postTranslate(xLogo, yLogo)
            canvas.drawBitmap(logo, matrix, null)
            invalidate()
        }
    }
}