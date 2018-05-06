package com.example.wade8.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/6.
 */

public class GameDataDbHelper extends SQLiteOpenHelper{

    private static final String TAG = GameDataDbHelper.class.getSimpleName();

    public Context mContext;
    public static final String DATABASE_NAME = "gameData.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE =
              "CREATE TABLE " + Constants.GameDataDBContract.TABLE_NAME +"("+
                        Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " INTEGER, " +
                        Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " INTEGER NOT NULL, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " TEXT, " +
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

    public GameDataDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void writeInitDataIntoDataBase(GameInfo gameInfo){
        int totalQuarter = Integer.parseInt(gameInfo.getTotalQuarter());
        SQLiteDatabase db = getWritableDatabase();
        for (Player mPlayer:gameInfo.getStartingPlayerList()){
            for (int i = 0; i<totalQuarter; i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,"temp"); //TODO real game ID
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER,i+1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,mPlayer.getmNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,mPlayer.getmName());
                db.insert(Constants.GameDataDBContract.TABLE_NAME,null,contentValues);
            }
        }
        for (Player mPlayer:gameInfo.getSubstitutePlayerList()){
            for (int i = 0; i<totalQuarter; i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,"temp"); //TODO real game ID
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER,i+1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,mPlayer.getmNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,mPlayer.getmName());
                db.insert(Constants.GameDataDBContract.TABLE_NAME,null,contentValues);
            }
        }
    }
}
