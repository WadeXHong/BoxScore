package com.example.wade8.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

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

        int value = mGameInfo.getDetailData()
                  .get(mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER))
                  .get(Integer.parseInt(mGameInfo.getStartingPlayerList().get(position).getmNumber())).get(type);

        mGameInfo.getDetailData()
                  .get(mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER))
                  .get(Integer.parseInt(mGameInfo.getStartingPlayerList().get(position).getmNumber())).put(type,value+1);

        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type),value+1);//TODO value
        int result = getWritableDatabase().update(Constants.GameDataDBContract.TABLE_NAME,cv,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " = ?",
                  new String[] {"temp",String.valueOf(gameInfo.getTeamData().get(Constants.RecordDataType.QUARTER)),gameInfo.getStartingPlayerList().get(position).getmNumber()});
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
}
