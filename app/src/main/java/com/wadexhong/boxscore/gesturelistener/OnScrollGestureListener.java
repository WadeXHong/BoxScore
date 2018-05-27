package com.wadexhong.boxscore.gesturelistener;

/**
 * Created by wade8 on 2018/5/14.
 */

public interface OnScrollGestureListener {
    void ScrollUp(int pointerCount);
    void ScrollDown(int pointerCount);
    void ScrollLeft(int pointerCount);
    void ScrollRight(int pointerCount);
}
