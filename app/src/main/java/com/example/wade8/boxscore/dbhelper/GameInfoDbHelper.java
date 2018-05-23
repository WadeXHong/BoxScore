package com.example.wade8.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.SharedPreferenceHelper;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/11.
 */

public class GameInfoDbHelper extends SQLiteOpenHelper{

    public static final String TAG = GameInfoDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "gameInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE =
              "CREATE TABLE "+ Constants.GameInfoDBContract.TABLE_NAME + "(" +
                        Constants.GameInfoDBContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.GameInfoDBContract.GAME_ID + " TEXT NOT NULL, " +
                        Constants.GameInfoDBContract.GAME_NAME + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.YOUR_TEAM + " TEXT DEFAULT 'NO TEAM', " +
                        Constants.GameInfoDBContract.OPPONENT_NAME + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.QUARTER_LENGTH + " INTEGER DEFAULT 12, " +
                        Constants.GameInfoDBContract.TOTAL_QUARTER + " INTEGER DEFAULT 4, " +
                        Constants.GameInfoDBContract.MAX_FOUL + " INTEGER DEFAULT 5, " +
                        Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF + " INTEGER DEFAULT 2, " +
                        Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF + " INTEGER DEFAULT 3, " +
                        Constants.GameInfoDBContract.YOUR_TEAM_SCORE + " INTEGER DEFAULT 0, " +
                        Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE + " INTEGER DEFAULT 0, " +
                        Constants.GameInfoDBContract.GAME_DATE + " TEXT DEFAULT '', " +
                        Constants.GameInfoDBContract.IS_GAMEOVER + " BOOLEAN NOT NULL DEFAULT 0" + ");";

    private GameInfo mGameInfo;

    public GameInfoDbHelper(Context context){
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

    public void setGameInfo(GameInfo mGameInfo) {
        this.mGameInfo = mGameInfo;
    }

    public void writeGameInfoIntoDataBase() {
        ContentValues cv = new ContentValues();
        cv.put(Constants.GameInfoDBContract.GAME_ID,mGameInfo.getGameId());
        cv.put(Constants.GameInfoDBContract.GAME_NAME,mGameInfo.getGameName());
        cv.put(Constants.GameInfoDBContract.YOUR_TEAM,mGameInfo.getYourTeam());
        cv.put(Constants.GameInfoDBContract.OPPONENT_NAME,mGameInfo.getOpponentName());
        cv.put(Constants.GameInfoDBContract.QUARTER_LENGTH,mGameInfo.getQuarterLength());
        cv.put(Constants.GameInfoDBContract.TOTAL_QUARTER,mGameInfo.getTotalQuarter());
        cv.put(Constants.GameInfoDBContract.MAX_FOUL,mGameInfo.getMaxFoul());
        cv.put(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF,mGameInfo.getTimeoutFirstHalf());
        cv.put(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF,mGameInfo.getTimeoutSecondHalf());
        cv.put(Constants.GameInfoDBContract.GAME_DATE,mGameInfo.getGameDate());

        getWritableDatabase().insert(Constants.GameInfoDBContract.TABLE_NAME,null,cv);
        getWritableDatabase().close();
    }

    public Cursor getGameInfo(){
        return getReadableDatabase()
                  .query(Constants.GameInfoDBContract.TABLE_NAME,
                            null,
                            Constants.GameInfoDBContract.GAME_ID+" =?",
                            new String[]{SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,null)},
                            null,null, null);
    }

    public void removeGameInfo(String gameId) {
        if (!gameId.equals("")){
            getWritableDatabase().delete(Constants.GameInfoDBContract.TABLE_NAME, Constants.GameInfoDBContract.GAME_ID + " = ?", new String[]{gameId});
        }
    }

    public Cursor getGameHistory() {
        return getReadableDatabase()
                  .query(Constants.GameInfoDBContract.TABLE_NAME,
                            null,
                            null,
                            null
                            , null, null, Constants.GameInfoDBContract.GAME_DATE+" DESC");
    }

    public void writeGameData(int type) {
        ContentValues cv = new ContentValues();
        switch (type){

//            case Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE:
            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:
            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:
            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:
            case Constants.RecordDataType.MINUS_FREE_THROW_MADE:
            case Constants.RecordDataType.MINUS_TWO_POINT_MADE:
            case Constants.RecordDataType.MINUS_THREE_POINT_MADE:

                cv.put(Constants.GameInfoDBContract.YOUR_TEAM_SCORE, mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE));
                getWritableDatabase().update(Constants.GameInfoDBContract.TABLE_NAME,
                          cv,
                          Constants.GameInfoDBContract.GAME_ID + " =?",
                          new String[]{mGameInfo.getGameId()});
                break;

            case Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE:
                cv.put(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE, mGameInfo.getTeamData().get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
                getWritableDatabase().update(Constants.GameInfoDBContract.TABLE_NAME,
                          cv,
                          Constants.GameInfoDBContract.GAME_ID + " =?",
                          new String[]{mGameInfo.getGameId()});
            break;

        }
    }

    public Cursor getHistoryInfo(String gameId) {
        return getReadableDatabase().query(Constants.GameInfoDBContract.TABLE_NAME, null,
                  Constants.GameInfoDBContract.GAME_ID + " =?",
                  new String[]{gameId}, null, null, null);
    }
}
