package com.wadexhong.boxscore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.SharedPreferenceHelper;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.Undo;

import java.util.ArrayList;
import java.util.List;

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
                        Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " INTEGER NOT NULL, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME +" TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_POINTS + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_ASSIST + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_STEAL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_BLOCK + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + " INTEGER DEFAULT 0, " +
                        Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + " INTEGER DEFAULT 0" +");";


    private GameInfo mGameInfo;
    private SparseIntArray mTeamData;
    private List<Undo> mUndoList;

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
            for (int i = 0; i<4; i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID, mPlayer.getPlayerId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,mGameInfo.getGameId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER,i+1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,mPlayer.getNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,mPlayer.getName());
                db.insert(Constants.GameDataDBContract.TABLE_NAME,null,contentValues);
            }
        }
        for (Player mPlayer:mGameInfo.getSubstitutePlayerList()){
            for (int i = 0; i<4; i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID, mPlayer.getPlayerId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,mGameInfo.getGameId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER,i+1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,mPlayer.getNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,mPlayer.getName());
                db.insert(Constants.GameDataDBContract.TABLE_NAME,null,contentValues);
            }
        }
    }

    public void setUndoList(List<Undo> mUndoList) {
        this.mUndoList = mUndoList;
    }

    public void undoGameData(int position) {
        int type = mUndoList.get(position).getType();
        if (type < Constants.RecordDataType.JUDGEMENT_NUMBER){ //type < 0x1000代表 原本輸入的指令為 increase, 要undo要decrease
            decreaseData(position, type);
        }else {
            increaseData(position, type);
        }

    }

    public void writeGameData(int position, int type){

        if (type < Constants.RecordDataType.JUDGEMENT_NUMBER){
            increaseData(position, type);
        }else {
            decreaseData(position, type);
        }


    }

    private void decreaseData(int position, int type) {

        int playerNumber;
        int quarter;

        if (type < Constants.RecordDataType.JUDGEMENT_NUMBER){ //代表此type為原始記錄在UndoList裡的項目,號碼須由UndoList的position取出
            playerNumber = Integer.parseInt(mUndoList.get(position).getPlayer().getNumber());
            quarter = mUndoList.get(position).getQuarter();
        }else { //代表此type為"原本就下令要針對特定球員修正數據",號碼由Dialog顯示場上球員position取出
            playerNumber = Integer.parseInt(mGameInfo.getStartingPlayerList().get(position).getNumber());
            quarter = mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER);
            type -= Constants.RecordDataType.JUDGEMENT_NUMBER; // type減去 0x1000, 變為increase type, 再對increaseData中的方法由+改為-就可延用
        }

        ContentValues cv = new ContentValues();
        int points;
        int FTAttend;
        int FGAttend;
        int TPAttend;
        int teamScore;
        switch (type){
            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:
                FTAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                if (FTAttend == 0 || points == 0 || teamScore ==0 )return;
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.FREE_THROW_SHOT_MISSED,FTAttend-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points-1); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED),FTAttend-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points-1);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)-1);
                break;

            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                if (FGAttend == 0 || points == 0 || teamScore ==0 )return;
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore-2);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points-2); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points-2);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)-2);

                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MISSED:
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                if (FGAttend == 0)return;
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend-1);
                break;


            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:
                int FGMade = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MADE);
                TPAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                if (FGMade == 0 || TPAttend == 0 || FGAttend == 0 || points == 0 || teamScore == 0 )return;
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore-3);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MADE,FGMade-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.THREE_POINT_SHOT_MISSED,TPAttend-1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points-3); //point key = 0;

                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MADE),FGMade-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED),TPAttend-1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points-3);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)-3);

                break;

        }//TODO Foul
        int value = mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).get(type);
        if (value == 0)return;
        mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).put(type,value-1);

        cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type),value-1);//TODO value
        int result = getWritableDatabase().update(Constants.GameDataDBContract.TABLE_NAME,cv,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " = ?",
                  new String[] {mGameInfo.getGameId(),String.valueOf(quarter),String.valueOf(playerNumber)});
        Log.d(TAG,"result = "+result);
    }

    private void increaseData(int position, int type) {

        int playerNumber;
        int quarter;

        if (type > Constants.RecordDataType.JUDGEMENT_NUMBER){ //代表此type為原始記錄在UndoList裡的項目,號碼須由UndoList的position取出
            playerNumber = Integer.parseInt(mUndoList.get(position).getPlayer().getNumber());
            quarter = mUndoList.get(position).getQuarter();
            type -= Constants.RecordDataType.JUDGEMENT_NUMBER; // type減去 0x1000, 讓decrease的undo顯示increase type
        }else { //代表此type為"原本就下令要針對特定球員修正數據",號碼由Dialog顯示場上球員position取出
            playerNumber = Integer.parseInt(mGameInfo.getStartingPlayerList().get(position).getNumber());
            quarter = mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER);

        }

        ContentValues cv = new ContentValues();
        int points;
        int FTAttend;
        int FGAttend;
        int TPAttend;
        int teamScore;
        switch (type){
            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:
                FTAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.FREE_THROW_SHOT_MISSED,FTAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+1); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED),FTAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+1);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)+1);
                break;

            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore+2);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+2); //point key = 0;
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+2);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)+2);
                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MISSED:
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend+1);
                break;


            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:
                int FGMade = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MADE);
                TPAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);
                FGAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);
                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);
                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,teamScore+3);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MADE,FGMade+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED,FGAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.THREE_POINT_SHOT_MISSED,TPAttend+1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0,points+3); //point key = 0;

                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MADE),FGMade+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED),FGAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED),TPAttend+1);
                cv.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0),points+3);
                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0)+3);
                break;

        }//TODO Foul
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
                  new String[] {mGameInfo.getGameId(),String.valueOf(quarter),String.valueOf(playerNumber)});
        Log.d(TAG,"result = "+result);
    }

    public void setGameInfo(GameInfo mGameInfo) {
        this.mGameInfo = mGameInfo;
    }

    public ArrayList<Player> setPlayerListFromDb(){

        Cursor cursor = getReadableDatabase()
                  .query(Constants.GameDataDBContract.TABLE_NAME,
                            new String[]{Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID},
                            Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" =?",
                            new String[]{mGameInfo.getGameId()},
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,null,null);

        ArrayList<Player> playerList = new ArrayList<>();
        int size = cursor.getCount();

        for (int i=0; i<size; i++){
            cursor.moveToPosition(i);
            playerList.add(new Player(cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER)),
                      cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME)),
                      cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID))));
        }

        cursor.close();

        mGameInfo.setStartingPlayerList(new ArrayList<Player>(playerList.subList(0,5)));
        mGameInfo.setSubstitutePlayerList(new ArrayList<Player>(playerList.subList(5,size)));

        return playerList;
    }

    public void setDetailDataFromDb() {

        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        SparseArray<SparseArray<SparseIntArray>> mQuarterSparseArray = new SparseArray();

        for (int i=0; i < 4;i++){ //用 totalQuarter雖然是dynamic, 但是因為後續歷史紀錄以及FireBase難以用dynamic設計，因此改為用最大值4節

            SparseArray<SparseIntArray> mPlayerSparseArray = new SparseArray<SparseIntArray>();

            for (Player mPlayer:mGameInfo.getStartingPlayerList()){

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                Cursor cursor = getReadableDatabase()
                          .query(Constants.GameDataDBContract.TABLE_NAME,null,
                                    Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ? AND "+Constants.GameDataDBContract.COLUMN_NAME_QUARTER+" = ? AND "+Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER+"= ?",
                                    new String[]{mGameInfo.getGameId(),String.valueOf(i+1),mPlayer.getNumber()},
                                    null,null,null);
                cursor.moveToFirst();
                mDataSparseIntArray.append(Constants.RecordDataType.POINTS,cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS)));
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND)));
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND)));
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_ASSIST)));
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_STEAL)));
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_BLOCK)));
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL)));
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER)));
                cursor.close();

                int key =Integer.parseInt(mPlayer.getNumber());

                mPlayerSparseArray.put(key,mDataSparseIntArray);
            }

            for (Player mPlayer:mGameInfo.getSubstitutePlayerList()){

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                Cursor cursor = getReadableDatabase()
                          .query(Constants.GameDataDBContract.TABLE_NAME,null,
                                    Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ? AND "+Constants.GameDataDBContract.COLUMN_NAME_QUARTER+" = ? AND "+Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER+"= ?",
                                    new String[]{mGameInfo.getGameId(),String.valueOf(i+1),mPlayer.getNumber()},
                                    null,null,null);
                cursor.moveToFirst();
                mDataSparseIntArray.append(Constants.RecordDataType.POINTS,cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS)));
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MADE, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE)));
                mDataSparseIntArray.append(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED)));
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND)));
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND)));
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_ASSIST)));
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_STEAL)));
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_BLOCK)));
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL)));
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER)));
                cursor.close();

                int key =Integer.parseInt(mPlayer.getNumber());

                mPlayerSparseArray.put(key,mDataSparseIntArray);
            }

            mQuarterSparseArray.append(i+1, mPlayerSparseArray);
        }

        mGameInfo.setDetailData(mQuarterSparseArray);
    }

    public void writeInitDataIntoGameInfo() {

        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        SparseArray<SparseArray<SparseIntArray>> mQuarterSparseArray = new SparseArray();

        for (int i=0; i < 4;i++){  //用 totalQuarter雖然是dynamic, 但是因為後續歷史紀錄以及FireBase難以用dynamic設計，因此改為用最大值4節

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
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, 0);

                int key =Integer.parseInt(mPlayer.getNumber());

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
                mDataSparseIntArray.append(Constants.RecordDataType.OFFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.DEFENSIVE_REBOUND, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.ASSIST, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.STEAL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.BLOCK, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.FOUL, 0);
                mDataSparseIntArray.append(Constants.RecordDataType.TURNOVER, 0);

                int key =Integer.parseInt(mPlayer.getNumber());

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
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_ASSIST + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_STEAL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_BLOCK + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + ")"
                  },
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID+" = ?",
                  new String[]{mGameInfo.getGameId()}, //TODO dynamic gameid  shoulb be mGameInfo.getId
                  Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,null, Constants.GameDataDBContract.COLUMN_NAME_POINTS);

        return cursor;
    }

    public void removeGameData(String gameId) {
        if (!gameId.equals("")) {
            getWritableDatabase().delete(Constants.GameDataDBContract.TABLE_NAME,Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ?",new String[]{gameId});
        }
    }

    public Cursor getHistoryStatisic(String selection, String[] selectionArgs, @Nullable String groupBy, @Nullable String orderBy){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Constants.GameDataDBContract.TABLE_NAME,
                  new String[]{
                            Constants.GameDataDBContract.COLUMN_NAME_GAME_ID,
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME,
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_ASSIST + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_STEAL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_BLOCK + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL + ")",
                            "SUM(" + Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + ")"
                  },
                  selection,
                  selectionArgs, //TODO dynamic gameid  shoulb be mGameInfo.getId
                  groupBy,null, orderBy);

        return cursor;
    }

    public Cursor getSpecificGameData(String gameId){
        return getReadableDatabase().query(Constants.GameDataDBContract.TABLE_NAME,
                  null,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                  new String[]{gameId},
                  null, null, null);
    }

    public void deleteAll(){
        getWritableDatabase().delete(Constants.GameDataDBContract.TABLE_NAME, null, null);
    }
}
