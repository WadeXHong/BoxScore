package com.wadexhong.boxscore.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/31.
 */

public class ProgressBarDialog extends Dialog {

    private static ProgressBarDialog mInstance;
    private static TextView mTextView;

    private ProgressBarDialog(@NonNull Context context) {
        super(context,R.style.ProgressBarDialog);
        setContentView(R.layout.view_progressbar);
        mTextView = (TextView) findViewById(R.id.dialog_progress_bar_message);
        setCancelable(false);
    }

    public static void showProgressBarDialog(Context context, String message){
        if (mInstance == null && context == null)return;
        if (mInstance == null) mInstance = new ProgressBarDialog(context);
        mInstance.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
        mTextView.setText(message);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        mTextView.startAnimation(anim);
        if (!mInstance.isShowing())mInstance.show();
    }

    public static void hideProgressBarDialog(){
        if (mInstance != null && mInstance.isShowing()){
            mInstance.dismiss();
            BoxScore.sIsOnClickAllowed = true;
        }
    }

    public static void setNull(){
        mInstance = null;
    }
}
