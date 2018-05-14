package com.example.wade8.boxscore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wade8 on 2018/5/14.
 */

public class SharedPreferenceHelper {

    private static final SharedPreferenceHelper mSharedPreferenceHelper = getInstance() ;
    private static SharedPreferences mSharedPreferences;

    private SharedPreferenceHelper(){
    }

    public static SharedPreferenceHelper getInstance() {
        return mSharedPreferenceHelper;
    }

    public static final String PLAYING_GAME = "PLAYING_GAME";

    public static void init(Context context){
        if (mSharedPreferences == null){
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static String read(String key, String defValue){
        return mSharedPreferences.getString(key,defValue);
    }

    public static void write(String key, String value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void clear(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
