package com.bardxhong.boxscore.modelhelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wade8 on 2018/5/14.
 */

public class SharedPreferenceHelper {

    private static final SharedPreferenceHelper mSharedPreferenceHelper = getInstance();

    private static SharedPreferences mSharedPreferences;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
              PLAYING_GAME,
              YOUR_TEAM_ID,
              YOUR_TEAM_TOTAL_SCORE,
              OPPONENT_TEAM_TOTAL_SCORE,
              YOUR_TEAM_FOUL,
              OPPONENT_TEAM_FOUL,
              QUARTER,
              DOUBLE_UP,
              DOUBLE_DOWN,
              DOUBLE_LEFT,
              DOUBLE_RIGHT,
              TRIPLE_UP,
              TRIPLE_DOWN,
              TRIPLE_LEFT,
              TRIPLE_RIGHT,
              BRIGHTNESS
    })

    public @interface PrefKey {
    }

    public static final String PLAYING_GAME = "PLAYING_GAME";
    public static final String YOUR_TEAM_ID = "YOUR_TEAM_ID";
    public static final String YOUR_TEAM_TOTAL_SCORE = "YOUR_TEAM_TOTAL_SCORE";
    public static final String OPPONENT_TEAM_TOTAL_SCORE = "OPPONENT_TEAM_TOTAL_SCORE";
    public static final String YOUR_TEAM_FOUL = "YOUR_TEAM_FOUL";
    public static final String OPPONENT_TEAM_FOUL = "OPPONENT_TEAM_FOUL";
    public static final String QUARTER = "QUARTER";
    public static final String DOUBLE_UP = "DOUBLE_UP";
    public static final String DOUBLE_DOWN = "DOUBLE_DOWN";
    public static final String DOUBLE_LEFT = "DOUBLE_LEFT";
    public static final String DOUBLE_RIGHT = "DOUBLE_RIGHT";
    public static final String TRIPLE_UP = "TRIPLE_UP";
    public static final String TRIPLE_DOWN = "TRIPLE_DOWN";
    public static final String TRIPLE_LEFT = "TRIPLE_LEFT";
    public static final String TRIPLE_RIGHT = "TRIPLE_RIGHT";
    public static final String BRIGHTNESS = "BRIGHTNESS";


    private SharedPreferenceHelper() {
    }

    public static SharedPreferenceHelper getInstance() {
        return mSharedPreferenceHelper;
    }


    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static String read(@PrefKey String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public static int read(@PrefKey String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public static float read(@PrefKey String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public static void write(@PrefKey String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void write(@PrefKey String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void write(@PrefKey String key, float value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(@PrefKey String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean contains(@PrefKey String key) {
        return mSharedPreferences.contains(key);
    }
}