package com.wadexhong.boxscore.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/17.
 */

public class TransparentAlertDialog {
    private AlertDialog mAlertDialog;
    private Handler mHandler;
    private static TransparentAlertDialog mInstance;

    private TransparentAlertDialog(){
        super();
        mHandler = new Handler();
    }

    public static TransparentAlertDialog getInstance(){
        if(mInstance == null){
            mInstance = new TransparentAlertDialog();
        }
        return mInstance;
    }

    public void show(Context context, int millisecond){
        if (mAlertDialog != null && mAlertDialog.isShowing()){
            dismissTimer(millisecond);
            return;
        }
        mAlertDialog = new AlertDialog.Builder(context, R.style.TransparentAlertDialog).show();
        dismissTimer(millisecond);
    }

    private void dismissTimer(final int millisecond){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millisecond);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        }).start();
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
