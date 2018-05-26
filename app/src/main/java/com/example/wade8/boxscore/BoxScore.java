package com.example.wade8.boxscore;

import android.app.Application;
import android.content.Context;

import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;
import com.example.wade8.boxscore.dbhelper.GameInfoDbHelper;
import com.example.wade8.boxscore.dbhelper.TeamDbHelper;

/**
 * Created by wade8 on 2018/5/6.
 */

public class BoxScore extends Application {

    private static Context mContext;
    private static GameDataDbHelper mGameDataDbHelper;
    private static GameInfoDbHelper mGameInfoDbHelper;
    private static TeamDbHelper mTeamDbHelper;
    public static float sBrightness;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mGameDataDbHelper = null;
        SharedPreferenceHelper.init(this);
        sBrightness = SharedPreferenceHelper.read(SharedPreferenceHelper.BRIGHTNESS, -1f);
    }

    public static Context getAppContext(){return mContext;}

    public static GameDataDbHelper getGameDataDbHelper(){
        if (mGameDataDbHelper == null)mGameDataDbHelper = new GameDataDbHelper(getAppContext());
        return mGameDataDbHelper;
    }

    public static GameInfoDbHelper getGameInfoDbHelper(){
        if (mGameInfoDbHelper == null)mGameInfoDbHelper = new GameInfoDbHelper(getAppContext());
        return mGameInfoDbHelper;
    }

    public static TeamDbHelper getTeamDbHelper(){
        if (mTeamDbHelper == null) mTeamDbHelper = new TeamDbHelper(getAppContext());
        return mTeamDbHelper;
    }
}
