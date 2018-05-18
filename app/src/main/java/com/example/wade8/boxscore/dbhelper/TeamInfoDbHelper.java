package com.example.wade8.boxscore.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wade8.boxscore.Constants;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamInfoDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TeamInfoDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "teamInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE =
              "CREATE TABLE "+ Constants.GameInfoDBContract.TABLE_NAME + "(" +
                        Constants.TeamInfoDBContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TeamInfoDBContract.TEAM_NAME + " TEXT NOT NULL, " +
                        Constants.TeamInfoDBContract.PLAYER_NAME + " TEXT DEFAULT '', " +
                        Constants.TeamInfoDBContract.PLAYER_NUMBER + " TEXT NOT NULL, " +
                        Constants.TeamInfoDBContract.FLAG + " BOOLEAN NOT NULL DEFAULT ''" +");";
    public TeamInfoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
