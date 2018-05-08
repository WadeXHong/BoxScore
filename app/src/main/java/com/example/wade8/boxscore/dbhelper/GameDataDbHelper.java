package com.example.wade8.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
                        Constants.GameDataDBContract.COLUMN_NAME_POINTS + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_ASSIST + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_STEAL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_BLOCK + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + " INTEGER DEFAULT 0" + ");";

    private GameInfo mGameInfo;

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

    public void writeInitDataIntoDataBase(){
        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());
        SQLiteDatabase db = getWritableDatabase();
        for (Player mPlayer:mGameInfo.getStartingPlayerList()){
            for (int i = 0; i<totalQuarter; i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,"temp"); //TODO real game ID
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER,i+1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,mPlayer.getmNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,mPlayer.getmName());
                db.insert(Constants.GameDataDBContract.TABLE_NAME,null,contentValues);
            }
        }
        for (Player mPlayer:mGameInfo.getSubstitutePlayerList()){
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

    public void writeGameData(GameInfo gameInfo, int position, int type){

        int quarter = mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER);
        int playerNumber = Integer.parseInt(mGameInfo.getStartingPlayerList().get(position).getmNumber());
        ContentValues cv = new ContentValues();
        int points = 0;
        switch (type){
            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:
                int FTAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(type+1,FTAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+1); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type+1),FTAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+1);
                break;

            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:
                int FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(type+1,FGAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+2); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type+1),FGAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+2);

                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:
                int TPAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(type+1,TPAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+3); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type+1),TPAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+3);
                break;

        }
        int value = mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).get(type);

        mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).put(type,value+1);

        cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type),value+1);//TODO value
        int result = getWritableDatabase().update(Constants.GameDataDBContract.TABLE_NAME,cv,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " = ?",
                  new String[] {"temp",String.valueOf(quarter),String.valueOf(playerNumber)});
        Log.d(TAG,"result = "+result);

    }

    public void setGameInfo(GameInfo mGameInfo) {
        this.mGameInfo = mGameInfo;
    }

    public void writeInitDataIntoGameInfo() {

        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        SparseArray<SparseArray<SparseIntArray>> mQuarterSparseArray = new SparseArray();

        for (int i=0; i<totalQuarter;i++){

            SparseArray<SparseIntArray> mPlayerSparseArray = new SparseArray<SparseIntArray>();

            for (Player mPlayer:mGameInfo.getStartingPlayerList()){

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                mDataSparseIntArray.append(Constants.RecordDataType.POINTS, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, 0);

                int key =Integer.parseInt(mPlayer.getmNumber());

                mPlayerSparseArray.put(key,mDataSparseIntArray);
            }

            for (Player mPlayer:mGameInfo.getSubstitutePlayerList()){

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                mDataSparseIntArray.append(Constants.RecordDataType.POINTS, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MADE, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, 0);

                int key =Integer.parseInt(mPlayer.getmNumber());

                mPlayerSparseArray.put(key,mDataSparseIntArray);
            }

            mQuarterSparseArray.append(i+1, mPlayerSparseArray);
        }

        mGameInfo.setDetailData(mQuarterSparseArray);

    }

    public Cursor getGameStatisic(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Constants.GameDataDBContract.TABLE_NAME,
                  new String[]{
                            Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_ASSIST + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_STEAL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_BLOCK + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + ")"
                  },
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ?",
                  new String[]{"temp"}, //TODO dynamic gameid  shoulb be mGameInfo.getId
                  Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,null, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER);

        return cursor;
    }
}
