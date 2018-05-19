package com.example.wade8.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.objects.Team;

import java.util.UUID;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TeamDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "teamInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE_TEAMINFO =
              "CREATE TABLE "+ Constants.TeamInfoDBContract.TABLE_NAME + "(" +
                        Constants.TeamInfoDBContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TeamInfoDBContract.TEAM_NAME + " TEXT NOT NULL, " +
                        Constants.TeamInfoDBContract.TEAM_ID + " TEXT" +");";

    public static final String SQL_CREATE_TEAMPLAYER =
              "CREATE TABLE "+ Constants.TeamPlayersContract.TABLE_NAME + "(" +
                        Constants.TeamPlayersContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TeamPlayersContract.PLAYER_ID + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.PLAYER_NAME + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.PLAYER_NUMBER + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.PLAY_C + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.PLAY_PF + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.PLAY_SF + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.PLAY_SG+ " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.PLAY_PG+ " BOOLEAN DEFAULT 0" + ");";


    public TeamDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TEAMINFO);
        db.execSQL(SQL_CREATE_TEAMPLAYER);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean createTeamInDB(String teamName){
        Cursor cursor = getReadableDatabase().query(Constants.TeamInfoDBContract.TABLE_NAME,
                  new String[]{Constants.TeamInfoDBContract.TEAM_NAME},
                  Constants.TeamInfoDBContract.TEAM_NAME+" =?",
                  new String[]{teamName},null,null,null);

        if (cursor.getCount() == 0){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TeamInfoDBContract.TEAM_NAME,teamName);
            contentValues.put(Constants.TeamInfoDBContract.TEAM_ID, UUID.randomUUID().toString());
            // TODO 未來新增球隊的輸入資料放在這

            getWritableDatabase().insert(Constants.TeamInfoDBContract.TABLE_NAME, null, contentValues);
            getWritableDatabase().close();

            return true;
        }else {
            return false;
        }
    }
}
