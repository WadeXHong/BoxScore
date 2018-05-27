package com.wadexhong.boxscore.customlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by wade8 on 2018/5/13.
 */

public class BSViewPager extends ViewPager {
    private static final String TAG = BSViewPager.class.getSimpleName();
    private final float mScaledPagingTouchSlop;
    private float mInitialPositionX;
    private boolean mIsScrollAllowed = true;

    public BSViewPager(@NonNull Context context) {
        super(context);
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public BSViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Log.d(TAG,"dispatchTouchEvent executed");
        int action = ev.getActionMasked();
        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG,"ACTION_DOWN executed");
//                break;
////            case MotionEvent.ACTION_MOVE:
////                Log.d(TAG,"ACTION_MOVE executed");
////                break;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG,"ACTION_UP executed");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.d(TAG,"ACTION_POINTER_DOWN executed");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                Log.d(TAG,"ACTION_POINTER_UP executed");
//                break;
        }

        boolean returnValue = super.dispatchTouchEvent(ev);
        Log.d(TAG,"dispatchTouchEvent return : " + returnValue);

        return returnValue;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        Log.e(TAG,"onInterceptTouchEvent executed");

        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"ACTION_DOWN executed");
                mInitialPositionX = ev.getX(0);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"ACTION_MOVE executed");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG,"ACTION_POINTER_DOWN executed");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"ACTION_POINTER_UP executed");
                break;
        }

        boolean returnValue = super.onInterceptTouchEvent(ev);
        Log.d(TAG,"onInterceptTouchEvent return : " + returnValue);

        return returnValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.w(TAG,"onTouchEvent executed");
        if (!mIsScrollAllowed){
            float dx = ev.getX(0)-mInitialPositionX;
            switch (getCurrentItem()){
                case 0:
                case 1:

                    if (dx<0) {
                        MotionEvent mMotionEvent = MotionEvent.obtain(ev);

                        mMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
                        return super.onTouchEvent(mMotionEvent);
                    }

                    break;
            }
        }

        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG,"ACTION_DOWN executed");
                break;
                case MotionEvent.ACTION_MOVE:
                    Log.w(TAG,"ACTION_MOVE executed");
                    break;
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

        boolean returnValue = super.onTouchEvent(ev);
//        Log.d(TAG,"onTouchEvent return : " + returnValue);

        return returnValue;
    }

    public boolean isScrollAllowed() {
        return mIsScrollAllowed;
    }

    public void setScrollAllowed(boolean scrollAllowed) {
        mIsScrollAllowed = scrollAllowed;
    }
}
