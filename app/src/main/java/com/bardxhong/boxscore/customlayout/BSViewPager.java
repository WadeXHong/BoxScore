package com.bardxhong.boxscore.customlayout;

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
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }


    public boolean isScrollAllowed() {
        return mIsScrollAllowed;
    }

    public void setScrollAllowed(boolean scrollAllowed) {
        mIsScrollAllowed = scrollAllowed;
    }

    /**
     * 若不允許滑動(mIsScrollAllowed = false) 的情況下，同時又處於向右滑動(dx<0)的情形下，利用MotionEvent.obtain
     * 新增一個 action 為 ACTION_CANCEL 的觸控事件並傳遞給父類別，來中斷ViewPager目前的滑動狀態
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.w(TAG, "onTouchEvent executed");

        if (!mIsScrollAllowed) {
            float dx = ev.getX(0) - mInitialPositionX;

            switch (getCurrentItem()) {
                case 0:
                case 1:

                    if (dx < 0) {
                        MotionEvent motionEvent = MotionEvent.obtain(ev);
                        motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                        return super.onTouchEvent(motionEvent);
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent executed");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent executed");

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mInitialPositionX = ev.getX(0);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void showActionMaskedLog(MotionEvent ev) {
        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "ACTION_DOWN executed");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "ACTION_MOVE executed");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "ACTION_POINTER_DOWN executed");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.w(TAG, "ACTION_POINTER_UP executed");
                break;
        }
    }
}
