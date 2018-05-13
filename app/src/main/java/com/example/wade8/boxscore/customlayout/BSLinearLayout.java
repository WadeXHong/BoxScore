package com.example.wade8.boxscore.customlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wade8 on 2018/5/12.
 */

public class BSLinearLayout extends LinearLayout{
    private static final String TAG = BSLinearLayout.class.getSimpleName();

    public BSLinearLayout(Context context) {
        super(context);
    }

    public BSLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BSLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent executed");
        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"ACTION_DOWN executed");
                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG,"ACTION_MOVE executed");
//                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG,"ACTION_POINTER_DOWN executed");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG,"ACTION_POINTER_UP executed");
                break;
        }

        boolean returnValue = super.dispatchTouchEvent(ev);
        Log.d(TAG,"dispatchTouchEvent return : " + returnValue);

        return returnValue;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.e(TAG,"onInterceptTouchEvent executed");

        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"ACTION_DOWN executed");
                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG,"ACTION_MOVE executed");
//                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG,"ACTION_POINTER_DOWN executed");
//                return true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"ACTION_POINTER_UP executed");
                break;
        }


        boolean returnValue = super.onInterceptTouchEvent(ev);
        Log.d(TAG,"onInterceptTouchEvent return : " + returnValue);

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG,"onTouchEvent executed");

        int action = event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG,"ACTION_DOWN executed");
                break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.w(TAG,"ACTION_MOVE executed");
//                    break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG,"ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG,"ACTION_POINTER_DOWN executed");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.w(TAG,"ACTION_POINTER_UP executed");
                break;
        }

        boolean returnValue = super.onTouchEvent(event);
        Log.d(TAG,"onTouchEvent return : " + returnValue);

        return false;
    }

}
