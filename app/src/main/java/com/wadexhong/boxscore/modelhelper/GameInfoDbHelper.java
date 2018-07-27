package com.wadexhong.boxscore.modelhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.objects.GameInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/11.
 */

public class GameInfoDbHelper extends SQLiteOpenHelper {

    public static final String TAG = GameInfoDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "gameInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE =
              "CREATE TABLE " + Constants.GameInfoDBContract.TABLE_NAME + "(" +
                        Constants.GameInfoDBContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " TEXT NOT NULL, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID + " TEXT DEFAULT 'quick_game', " +
                        Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM + " TEXT DEFAULT 'NO TEAM', " +
                        Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH + " INTEGER DEFAULT 12, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER + " INTEGER DEFAULT 4, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL + " INTEGER DEFAULT 5, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF + " INTEGER DEFAULT 2, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF + " INTEGER DEFAULT 3, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE + " INTEGER DEFAULT 0, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE + " INTEGER DEFAULT 0, " +
                        Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER + " BOOLEAN NOT NULL DEFAULT 0" + ");";

    private GameInfo mGameInfo;

    public GameInfoDbHelper(Context context) {
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

    public void setGameInfo(GameInfo mGameInfo) {
        this.mGameInfo = mGameInfo;
    }

    public Cursor getGameInfo() {
        return getReadableDatabase()
                  .query(Constants.GameInfoDBContract.TABLE_NAME,
                            null,
                            Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " =?",
                            new String[]{SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, null)},
                            null, null, null);
    }

    public void writeGameInfoIntoDataBase() {

        ContentValues cv = new ContentValues();
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID, mGameInfo.getGameId());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME, mGameInfo.getGameName());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID, mGameInfo.getYourTeamId());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM, mGameInfo.getYourTeam());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME, mGameInfo.getOpponentName());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH, mGameInfo.getQuarterLength());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER, mGameInfo.getTotalQuarter());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL, mGameInfo.getMaxFoul());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF, mGameInfo.getTimeoutFirstHalf());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF, mGameInfo.getTimeoutSecondHalf());
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE, mGameInfo.getGameDate());

        getWritableDatabase().insert(Constants.GameInfoDBContract.TABLE_NAME, null, cv);
        getWritableDatabase().close();
    }

    public int deleteGameInfo(String teamId, String gameId) {
        if (!gameId.equals("")) {
            getWritableDatabase()
                      .delete(Constants.GameInfoDBContract.TABLE_NAME, Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " = ?", new String[]{gameId});

            if (teamId != null) {
                Cursor cursor = getReadableDatabase().query(Constants.GameInfoDBContract.TABLE_NAME,
                          null,
                          Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID + " =?",
                          new String[]{teamId},
                          null, null, null);

                return cursor.getCount();
            }
        }
        return -1;
    }

    public void writeGameData(int type) {
        ContentValues cv = new ContentValues();
        switch (type) {

//            case Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE:
            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:
            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:
            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:
            case Constants.RecordDataType.MINUS_FREE_THROW_MADE:
            case Constants.RecordDataType.MINUS_TWO_POINT_MADE:
            case Constants.RecordDataType.MINUS_THREE_POINT_MADE:

                cv.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE, mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE));
                getWritableDatabase().update(Constants.GameInfoDBContract.TABLE_NAME,
                          cv,
                          Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " =?",
                          new String[]{mGameInfo.getGameId()});
                break;

            case Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE:
                cv.put(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE, mGameInfo.getTeamData().get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
                getWritableDatabase().update(Constants.GameInfoDBContract.TABLE_NAME,
                          cv,
                          Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " =?",
                          new String[]{mGameInfo.getGameId()});
                break;

        }
    }

    public Cursor getGameHistory() {
        return getReadableDatabase()
                  .query(Constants.GameInfoDBContract.TABLE_NAME,
                            null,
                            Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER + " =?",
                            new String[]{"1"}
                            , null, null, Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE + " DESC");
    }

    public ArrayList<String> getGameHistoryList() {

        Cursor cursor = getGameHistory();
        ArrayList<String> gameHistoryList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            gameHistoryList.add(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID)));
        }
        cursor.close();

        return gameHistoryList;
    }

    public Cursor getSpecificInfo(String gameId) {
        return getReadableDatabase().query(Constants.GameInfoDBContract.TABLE_NAME, null,
                  Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " =?",
                  new String[]{gameId}, null, null, null);
    }


    public int overPlayingGame(String gameId, String teamId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER, true);
        db.update(Constants.GameInfoDBContract.TABLE_NAME, cv, Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " =?", new String[]{gameId});

        Cursor cursor = getReadableDatabase().query(Constants.GameInfoDBContract.TABLE_NAME,
                  new String[]{Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID},
                  Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID + " =? AND " + Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER + " =?",
                  new String[]{teamId, "1"},
                  null, null, null);

        return cursor.getCount();
    }

    public void deleteAll(@Nullable String notEndedGameId) {

        if (notEndedGameId == null || notEndedGameId.equals("")) {
            getWritableDatabase().delete(Constants.GameInfoDBContract.TABLE_NAME, null, null);
        } else {
            getWritableDatabase().delete(Constants.GameInfoDBContract.TABLE_NAME,
                      Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " !=?",
                      new String[]{notEndedGameId});
        }
    }
}
