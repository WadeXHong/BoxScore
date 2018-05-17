package com.example.wade8.boxscore.objects;

import android.app.AlertDialog;
import android.content.Context;

import com.example.wade8.boxscore.R;

/**
 * Created by wade8 on 2018/5/17.
 */

public class TransparentAlertDialog {
    private AlertDialog mAlertDialog;
    private static TransparentAlertDialog mInstance;

    public static TransparentAlertDialog getInstance(){
        if(mInstance == null){
            mInstance = new TransparentAlertDialog();
        }
        return mInstance;
    }

    public void show(Context context){
        if (mAlertDialog != null && mAlertDialog.isShowing()){
            return;
        }
        mAlertDialog = new AlertDialog.Builder(context, R.style.TransparentAlertDialog).show();
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
