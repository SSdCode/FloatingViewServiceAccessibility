package com.sdcode.floatingball

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button


class FloatingViewServiceAccessibility : AccessibilityService() {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var floatingViewParams: WindowManager.LayoutParams? = null
    private var isDraggingEnabled = false

    override fun onCreate() {
        super.onCreate()

        floatingView =
            LayoutInflater.from(this).inflate(R.layout.floating_view_layout, null) as Button

        floatingViewParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        floatingViewParams!!.gravity = Gravity.TOP or Gravity.START
        floatingViewParams!!.x = 0
        floatingViewParams!!.y = 100

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager!!.addView(floatingView, floatingViewParams)


        floatingView!!.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var deltaX = 0
            private var deltaY = 0
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Get the initial position and touch coordinates
                        initialX = floatingViewParams!!.x
                        initialY = floatingViewParams!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDraggingEnabled = true
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (!isDraggingEnabled) {
                            return false
                        }
                        // Calculate the new position based on the touch coordinates
                        deltaX = (event.rawX - initialTouchX).toInt()
                        deltaY = (event.rawY - initialTouchY).toInt()
                        floatingViewParams!!.x = initialX + deltaX
                        floatingViewParams!!.y = initialY + deltaY

                        // Update the position of the floating view
                        windowManager!!.updateViewLayout(floatingView, floatingViewParams)
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        // Handle the click event on the floating view
                        val offsetX = (event.rawX - initialTouchX).toInt()
                        val offsetY = (event.rawY - initialTouchY).toInt()
                        if (Math.abs(offsetX) < 5 && Math.abs(offsetY) < 5) {
                            // Perform your desired action when the floating view is clicked
                            Log.d("samir", "Floating button clicked.")
                        }
                        isDraggingEnabled = false
                        return false
                    }
                }
                return false
            }
        })
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        TODO("Not yet implemented")
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }
}