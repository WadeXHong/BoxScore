package com.example.wade8.boxscore;

import android.app.Application;
import android.content.Context;

import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;

/**
 * Created by wade8 on 2018/5/6.
 */

public class BoxScore extends Application {

    private static Context mContext;
    private static GameDataDbHelper mGameDataDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mGameDataDbHelper = null;
    }

    public static Context getAppContext(){return mContext;}

    public static GameDataDbHelper getGameDataDbHelper(){
        if (mGameDataDbHelper == null)mGameDataDbHelper = new GameDataDbHelper(getAppContext());
        return mGameDataDbHelper;
    }
}
