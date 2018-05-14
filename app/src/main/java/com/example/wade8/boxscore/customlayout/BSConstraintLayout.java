package com.example.wade8.boxscore.customlayout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wade8 on 2018/5/13.
 */

public class BSConstraintLayout extends ConstraintLayout {
    public BSConstraintLayout(Context context) {
        super(context);
    }

    public BSConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BSConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
