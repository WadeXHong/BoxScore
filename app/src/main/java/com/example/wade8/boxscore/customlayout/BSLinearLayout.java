package com.example.wade8.boxscore.customlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.example.wade8.boxscore.gesturelistener.OnScrollGestureListener;

/**
 * Created by wade8 on 2018/5/12.
 */

public class BSLinearLayout extends LinearLayout{
    private static final String TAG = BSLinearLayout.class.getSimpleName();

    private OnScrollGestureListener mOnScrollGestureListener;

    private static final int TYPE_LEFT = 1;
    private static final int TYPE_RIGHT = 2;
    private static final int TYPE_UP = 3;
    private static final int TYPE_DOWN = 4;
    private final float mScaledPagingTouchSlop;
    private int mType;
    private int mMaxPointerCount;
    private float mInitPositionX;
    private float mInitPositionY;
    private float mLastPositionX;
    private float mLastPositionY;
    private float mDistanceX;
    private float mDistanceY;




    public BSLinearLayout(Context context) {
        super(context);
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();

    }

    public BSLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public BSLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaledPagingTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public void setOnScrollGestureListener(OnScrollGestureListener onScrollGestureListener){
        mOnScrollGestureListener = onScrollGestureListener;
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
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.e(TAG,"onInterceptTouchEvent executed");

        int action = ev.getActionMasked();

//        if (action == MotionEvent.ACTION_POINTER_DOWN){
//            return true;
//        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"ACTION_DOWN executed");
                mType = 0;
                mDistanceX = 0;
                mDistanceY = 0;
                mInitPositionX = mLastPositionX = ev.getX(0);
                mInitPositionY = mLastPositionY = ev.getY(0);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"ACTION_MOVE executed");
                if (ev.getPointerCount() == 1){
                    float x = ev.getX(0);
                    float y = ev.getY(0);
                    float dx = x - mLastPositionX;
                    float dy = y - mLastPositionY;
                    mLastPositionX = x;
                    mLastPositionY = y;
                    mDistanceX += Math.abs(dx);
                    mDistanceY += Math.abs(dy);

                    //單指滑動攔截
//                    if(mDistanceY> 2f* mDistanceX && mDistanceY> 3.0f*mScaledPagingTouchSlop){
//                        return true;
//                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP executed");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"ACTION_POINTER_UP executed");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG,"ACTION_POINTER_DOWN executed");
                return true;
        }


        boolean returnValue = super.onInterceptTouchEvent(ev);
        Log.d(TAG,"onInterceptTouchEvent return : " + returnValue);

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG,"onTouchEvent executed");
        int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        Log.w(TAG,"pointCount : "+pointerCount);

        mMaxPointerCount = pointerCount > mMaxPointerCount ? pointerCount:mMaxPointerCount;
        switch (action){

            case MotionEvent.ACTION_DOWN:
                Log.w(TAG,"ACTION_DOWN executed");
                mLastPositionX = event.getX(0);
                mLastPositionY = event.getY(0);
                break;

            case MotionEvent.ACTION_MOVE: //TODO  方向 and 距離
                Log.w(TAG,"ACTION_MOVE executed");

                for(int i=0 ; i < pointerCount; i++){
                    int pointerId = event.getPointerId(i);

                    if (pointerId == 0){ //只看原本的第一指
                        float x = event.getX(0);
                        float y = event.getY(0);
                        float dx = x - mLastPositionX;
                        float dy = y - mLastPositionY;
                        mLastPositionX = x;
                        mLastPositionY = y;
                        mDistanceX += Math.abs(dx);
                        mDistanceY += Math.abs(dy);
                        Log.e(TAG,"pointerIndex " + event.getPointerId(0));
                        if (mDistanceX + mDistanceY > 3.0f*mScaledPagingTouchSlop && Math.abs(x-mInitPositionX) + Math.abs(y-mInitPositionY) > mScaledPagingTouchSlop) {
                            if (mDistanceX > 2.0f * mDistanceY) { //左右滑動
                                if (x - mInitPositionX > 0) { //向右
                                    mType = TYPE_RIGHT;
                                } else if (x - mInitPositionX < 0) {//向左
                                    mType = TYPE_LEFT;
                                }
                            } else if (mDistanceX < 0.5f * mDistanceY) { //上下滑動
                                if (y - mInitPositionY < 0) {//向上
                                    mType = TYPE_UP;
                                } else if (y - mInitPositionY > 0) {//向下
                                    mType = TYPE_DOWN;
                                }
                            }
                        }
                        Log.e(TAG,"mType = "+mType);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.w(TAG,"ACTION_UP executed");

                if (mOnScrollGestureListener != null) {
                    switch (mType) {
                        case TYPE_UP:
                            mOnScrollGestureListener.ScrollUp(mMaxPointerCount);
                            break;
                        case TYPE_DOWN:
                            mOnScrollGestureListener.ScrollDown(mMaxPointerCount);
                            break;
                        case TYPE_LEFT:
                            mOnScrollGestureListener.ScrollLeft(mMaxPointerCount);
                            break;
                        case TYPE_RIGHT:
                            mOnScrollGestureListener.ScrollRight(mMaxPointerCount);
                            break;
                    }
                }
                mType = mMaxPointerCount = 0;
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

        return true;
    }

}
