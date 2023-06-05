package com.sdcode.floatingball;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class FloatingViewService extends AccessibilityService {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams floatingViewParams;
    private boolean isDraggingEnabled;


    @Override
    public void onCreate() {
        super.onCreate();



        // Inflate the floating view layout
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_view_layout, null);

        // Set up the WindowManager layout parameters for the floating view
        floatingViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatingViewParams.gravity = Gravity.TOP | Gravity.START;
        floatingViewParams.x = 0;
        floatingViewParams.y = 100;

        // Add the floating view to the window manager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, floatingViewParams);

        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("samir","Floating button clicked.");
            }
        });

        // Set up touch listener for the floating view to enable dragging
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private int deltaX;
            private int deltaY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Get the initial position and touch coordinates
                        initialX = floatingViewParams.x;
                        initialY = floatingViewParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isDraggingEnabled = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (!isDraggingEnabled) {
                            return false;
                        }
                        // Calculate the new position based on the touch coordinates
                        deltaX = (int) (event.getRawX() - initialTouchX);
                        deltaY = (int) (event.getRawY() - initialTouchY);
                        floatingViewParams.x = initialX + deltaX;
                        floatingViewParams.y = initialY + deltaY;

                        // Update the position of the floating view
                        windowManager.updateViewLayout(floatingView, floatingViewParams);

                        return true;
                    case MotionEvent.ACTION_UP:
                        // Handle the click event on the floating view
                        int offsetX = (int) (event.getRawX() - initialTouchX);
                        int offsetY = (int) (event.getRawY() - initialTouchY);
                        if (Math.abs(offsetX) < 5 && Math.abs(offsetY) < 5) {
                            // Perform your desired action when the floating view is clicked
                            // For example, launch an activity or show a popup
                        }
                        isDraggingEnabled = false;
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }
}
