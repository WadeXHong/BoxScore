package com.example.wade8.boxscore.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wade8.boxscore.Constants;

/**
 * Created by wade8 on 2018/5/6.
 */

public class GameDataDbHelper extends SQLiteOpenHelper{

    public Context mContext;
    public static final String DATABASE_NAME = "gameData.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE =
              "CREATE TABLE " + Constants.GameDataDBContract.TABLE_NAME +"("+
                        Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " INTEGER, " +
                        Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " INTEGER NOT NULL, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " INTEGER, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME +" TEXT, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_ASSIST + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_STEAL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_BLOCK + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + " INTEGER DEFAULT 0" + ");";

    public GameDataDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
