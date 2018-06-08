package com.wadexhong.boxscore;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;

import com.wadexhong.boxscore.modelhelper.GameDataDbHelper;
import com.wadexhong.boxscore.modelhelper.GameInfoDbHelper;
import com.wadexhong.boxscore.modelhelper.SharedPreferenceHelper;
import com.wadexhong.boxscore.modelhelper.TeamDbHelper;

/**
 * Created by wade8 on 2018/5/6.
 */

public class BoxScore extends Application {

    private static Context mContext;
    private static GameDataDbHelper mGameDataDbHelper;
    private static GameInfoDbHelper mGameInfoDbHelper;
    private static TeamDbHelper mTeamDbHelper;
    private static Vibrator mVibrator;
    public static float sBrightness;
    public static boolean sIsOnClickAllowed = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mGameDataDbHelper = null;
        SharedPreferenceHelper.init(this);
        sBrightness = SharedPreferenceHelper.read(SharedPreferenceHelper.BRIGHTNESS, -1f);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static GameDataDbHelper getGameDataDbHelper() {
        if (mGameDataDbHelper == null) mGameDataDbHelper = new GameDataDbHelper(getAppContext());
        return mGameDataDbHelper;
    }

    public static GameInfoDbHelper getGameInfoDbHelper() {
        if (mGameInfoDbHelper == null) mGameInfoDbHelper = new GameInfoDbHelper(getAppContext());
        return mGameInfoDbHelper;
    }

    public static TeamDbHelper getTeamDbHelper() {
        if (mTeamDbHelper == null) mTeamDbHelper = new TeamDbHelper(getAppContext());
        return mTeamDbHelper;
    }

    public static void vibrate() {
        if (Build.VERSION.SDK_INT < 26) {
            mVibrator.vibrate(20);
        } else {
            mVibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    public static String getStringEasy(@StringRes int StringId){
        return mContext.getString(StringId);
    }

    public static String getStringEasy(@StringRes int StringId, Object... formatArgs){
        return mContext.getString(StringId, formatArgs);
    }

    public static int getColorEasy(@ColorRes int colorId){
        return mContext.getResources().getColor(colorId);
    }

    public static float getDimensionEasy(@DimenRes int dimenId){
        return mContext.getResources().getDimension(dimenId);
    }

    public static boolean isOnClickAllowedAndSetTimer(){
        if (sIsOnClickAllowed) {
            onClickProcessingTimer(500);
            return true;
        }
        return false;
    }

    private static void onClickProcessingTimer(final int sleepTime){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sIsOnClickAllowed = false;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sIsOnClickAllowed = true;
            }
        }).start();
    }
}
