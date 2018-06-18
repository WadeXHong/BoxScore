package com.wadexhong.boxscore.modelhelper;

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
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.Undo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/6.
 */

public class GameDataDbHelper extends SQLiteOpenHelper {

    private static final String TAG = GameDataDbHelper.class.getSimpleName();

    public Context mContext;
    public static final String DATABASE_NAME = "gameData.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE =
              "CREATE TABLE " + Constants.GameDataDBContract.TABLE_NAME + "(" +
                        Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " INTEGER NOT NULL, " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " TEXT NOT NULL DEFAULT '', " +
                        Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME + " TEXT NOT NULL DEFAULT '', " +
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
                        Constants.GameDataDBContract.COLUMN_NAME_TURNOVER + " INTEGER DEFAULT 0" + ");";


    private GameInfo mGameInfo;
    private SparseIntArray mTeamData;

    public GameDataDbHelper(Context context) {
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

    public void setGameInfo(GameInfo gameInfo) {
        mGameInfo = gameInfo;
    }

    //Resume game status
    public ArrayList<Player> setPlayerListFromDb() {

        Cursor cursor = getReadableDatabase()
                  .query(Constants.GameDataDBContract.TABLE_NAME,
                            new String[]{Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID},
                            Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                            new String[]{mGameInfo.getGameId()},
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, null, null);

        ArrayList<Player> playerList = new ArrayList<>();
        int size = cursor.getCount();

        for (int i = 0; i < size; i++) {
            cursor.moveToPosition(i);
            playerList.add(new Player(cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER)),
                      cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME)),
                      cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID))));
        }

        cursor.close();

        mGameInfo.setStartingPlayerList(new ArrayList<Player>(playerList.subList(0, 5)));
        mGameInfo.setSubstitutePlayerList(new ArrayList<Player>(playerList.subList(5, size)));

        return playerList;
    }

    /**
     * Query data from DataBase and init 3D sparsearray for detail data of game.
     */
    public void setDetailDataFromDb() {

//        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        SparseArray<SparseArray<SparseIntArray>> mQuarterSparseArray = new SparseArray<>();

        for (int i = 0; i < 4; i++) { //用 totalQuarter雖然是dynamic, 但是因為後續歷史紀錄以及FireBase難以用dynamic設計，因此改為用最大值4節

            SparseArray<SparseIntArray> mPlayerSparseArray = new SparseArray<SparseIntArray>();

            for (Player mPlayer : mGameInfo.getStartingPlayerList()) {

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                Cursor cursor = getReadableDatabase()
                          .query(Constants.GameDataDBContract.TABLE_NAME, null,
                                    Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ? AND " +
                                              Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                                              Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + "= ?",
                                    new String[]{mGameInfo.getGameId(), String.valueOf(i + 1), mPlayer.getNumber()},
                                    null, null, null);
                cursor.moveToFirst();

                mDataSparseIntArray.append(Constants.RecordDataType.POINTS, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS)));
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

                int key = Integer.parseInt(mPlayer.getNumber());
                mPlayerSparseArray.put(key, mDataSparseIntArray);
            }

            for (Player mPlayer : mGameInfo.getSubstitutePlayerList()) {

                SparseIntArray mDataSparseIntArray = new SparseIntArray();

                Cursor cursor = getReadableDatabase()
                          .query(Constants.GameDataDBContract.TABLE_NAME, null,
                                    Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ? AND " +
                                              Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                                              Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + "= ?",
                                    new String[]{mGameInfo.getGameId(), String.valueOf(i + 1), mPlayer.getNumber()},
                                    null, null, null);
                cursor.moveToFirst();

                mDataSparseIntArray.append(Constants.RecordDataType.POINTS, cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS)));
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

                int key = Integer.parseInt(mPlayer.getNumber());

                mPlayerSparseArray.put(key, mDataSparseIntArray);
            }

            mQuarterSparseArray.append(i + 1, mPlayerSparseArray);
        }

        mGameInfo.setDetailData(mQuarterSparseArray);
    }

    /**
     * Insert players' initial data in database when new game starting.
     * Every player have four rows of  data.
     */
    public long writeInitDataIntoDataBase() {

//        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        long resultStartingPlayer = 0;
        long resultSubstitutePlayer = 0;
        SQLiteDatabase db = getWritableDatabase();

        for (Player mPlayer : mGameInfo.getStartingPlayerList()) {

            for (int i = 0; i < 4; i++) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID, mPlayer.getPlayerId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, mGameInfo.getGameId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER, i + 1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, mPlayer.getNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, mPlayer.getName());
                if (db.insert(Constants.GameDataDBContract.TABLE_NAME, null, contentValues) == -1)
                    resultStartingPlayer = -1;
            }
        }

        for (Player mPlayer : mGameInfo.getSubstitutePlayerList()) {

            for (int i = 0; i < 4; i++) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID, mPlayer.getPlayerId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, mGameInfo.getGameId());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER, i + 1);
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, mPlayer.getNumber());
                contentValues.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, mPlayer.getName());
                if (db.insert(Constants.GameDataDBContract.TABLE_NAME, null, contentValues) == -1)
                    resultSubstitutePlayer = -1;
            }
        }
        return resultStartingPlayer == -1 || resultSubstitutePlayer == -1 ? -1 : 0;
    }

    /**
     * Create and insert players' initial data in 3D-sparsearray when new game starting.
     * Every player have four rows of  data, structure is like quarters<players<data>>
     */
    public void writeInitDetailDataIntoGameInfo() {

//        int totalQuarter = Integer.parseInt(mGameInfo.getTotalQuarter());

        SparseArray<SparseArray<SparseIntArray>> mQuarterSparseArray = new SparseArray<>();

        for (int i = 0; i < 4; i++) {  //用 totalQuarter雖然是dynamic, 但是因為後續歷史紀錄以及FireBase難以用dynamic設計，因此改為用最大值4節

            SparseArray<SparseIntArray> mPlayerSparseArray = new SparseArray<SparseIntArray>();

            for (Player mPlayer : mGameInfo.getStartingPlayerList()) {

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

                int key = Integer.parseInt(mPlayer.getNumber());

                mPlayerSparseArray.put(key, mDataSparseIntArray);
            }

            for (Player mPlayer : mGameInfo.getSubstitutePlayerList()) {

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

                int key = Integer.parseInt(mPlayer.getNumber());

                mPlayerSparseArray.put(key, mDataSparseIntArray);
            }

            mQuarterSparseArray.append(i + 1, mPlayerSparseArray);
        }

        mGameInfo.setDetailData(mQuarterSparseArray);

    }

    /**
     * Return cursor of summary statistic of game data.
     *
     * @param groupBy 合併參數
     * @param orderBy 排序方式
     */
    public Cursor getHistoryStatistic(String selection, String[] selectionArgs, @Nullable String groupBy, @Nullable String orderBy) {

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
                  groupBy, null, orderBy);

        return cursor;
    }

    /**
     * Return cursor of  all game data for specific game, used in resuming game and synchronize with FireBase.
     */
    public Cursor getSpecificGameData(String gameId) {
        return getReadableDatabase().query(Constants.GameDataDBContract.TABLE_NAME,
                  null,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                  new String[]{gameId},
                  null, null, null);
    }

    /**
     * 增加數據數字, 用於一般數據紀錄以及操作紀錄的修改的後半部 (紀錄回填)
     * 以switch 判斷特殊數據紀錄情形例如: 三分球命中 需額外增加 field goal 出手以及命中
     * 增加行為包刮 :
     * 1. mGameInfo 中 DetailData 數據改變
     * 2. DetailData數據取出並 update database 特定 row
     * 3. 整隊類數據 (總分, 團犯等) 存入 SharedPreferences中
     *
     * @param player  for whom
     * @param type    for what
     * @param quarter for when
     */
    public void plusData(Player player, int type, int quarter) {

        int playerNumber = Integer.valueOf(player.getNumber());
        // quarter  = 0 代表為一般紀錄, 由 mGameInfo 取得當節節數
        // quarter != 0 為數據修改
        quarter = quarter == 0 ? mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER) : quarter;

        int points;
        int freeThrowAttend;
        int fieldGoalAttend;
        int threePointAttend;
        int teamScore;

        ContentValues contentValues = new ContentValues();

        switch (type) {

            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:

                freeThrowAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, freeThrowAttend + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points + 1); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED), freeThrowAttend + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points + 1);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) + 1);

                break;

            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore + 2);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points + 2); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points + 2);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) + 2);

                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MISSED:

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend + 1);

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend + 1);

                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:

                int FGMade = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MADE);

                threePointAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore + 3);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MADE, FGMade + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, threePointAttend + 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points + 3); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MADE), FGMade + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED), threePointAttend + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points + 3);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) + 3);

                break;

            case Constants.RecordDataType.OFFENSIVE_FOUL:

                int turnover = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TURNOVER);

                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TURNOVER, turnover + 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TURNOVER), turnover + 1);
                //type轉回FOUL，後續記錄至Db裡
                type = Constants.RecordDataType.FOUL;

                break;

            case Constants.RecordDataType.DEFENSIVE_FOUL:
                // 不是此節代表為歷史紀錄，已無關團犯。
                if (quarter == mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER)) {

                    int teamFoul = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_FOUL);

                    mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_FOUL, teamFoul + 1);
                    SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL, teamFoul + 1);
                }
                //type轉回FOUL，後續記錄至Db裡
                type = Constants.RecordDataType.FOUL;

                break;
        }//TODO 犯滿畢業
        int dataValue = mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).get(type);

        mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).put(type, dataValue + 1);

        contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type), dataValue + 1);//TODO value

        int result = getWritableDatabase().update(Constants.GameDataDBContract.TABLE_NAME,
                  contentValues,
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " = ?",
                  new String[]{mGameInfo.getGameId(), String.valueOf(quarter), String.valueOf(playerNumber)});

        Log.d(TAG, "result = " + result);
    }

    /**
     * 減少數據數字, 用於操作紀錄復原以及操作紀錄的修改的前半部 (紀錄刪除)
     * 以switch 判斷特殊數據紀錄情形例如: 三分球命中 需額外增加 field goal 出手以及命中
     * 增加行為包刮 :
     * 1. mGameInfo 中 DetailData 數據改變
     * 2. DetailData數據取出並 update database 特定 row
     * 3. 整隊類數據 (總分, 團犯等) 存入 SharedPreferences中
     *
     * @param player  for whom
     * @param type    for what
     * @param quarter for when
     */
    public void minusData(Player player, int type, int quarter) {

        int playerNumber = Integer.valueOf(player.getNumber());
//        quarter = quarter == 0 ? quarter : mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER);
        int points;
        int freeThrowAttend;
        int fieldGoalAttend;
        int threePointAttend;
        int teamScore;

        ContentValues contentValues = new ContentValues();

        switch (type) {

            case Constants.RecordDataType.FREE_THROW_SHOT_MADE:

                freeThrowAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                if (freeThrowAttend == 0 || points == 0 || teamScore == 0) return;

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.FREE_THROW_SHOT_MISSED, freeThrowAttend - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points - 1); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.FREE_THROW_SHOT_MISSED), freeThrowAttend - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points - 1);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) - 1);

                break;

            case Constants.RecordDataType.TWO_POINT_SHOT_MADE:

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                if (fieldGoalAttend == 0 || points == 0 || teamScore == 0) return;

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore - 2);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points - 2); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points - 2);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) - 2);

                break;

            case Constants.RecordDataType.THREE_POINT_SHOT_MISSED:

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                if (fieldGoalAttend == 0) return;

                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend - 1);

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend - 1);
                break;


            case Constants.RecordDataType.THREE_POINT_SHOT_MADE:

                int FGMade = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MADE);

                threePointAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);

                fieldGoalAttend = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);

                points = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.POINTS);

                teamScore = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE);

                if (FGMade == 0 || threePointAttend == 0 || fieldGoalAttend == 0 || points == 0 || teamScore == 0)
                    return;

                mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, teamScore - 3);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MADE, FGMade - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TWO_POINT_SHOT_MISSED, fieldGoalAttend - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.THREE_POINT_SHOT_MISSED, threePointAttend - 1);
                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(0, points - 3); //point key = 0;

                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MADE), FGMade - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TWO_POINT_SHOT_MISSED), fieldGoalAttend - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.THREE_POINT_SHOT_MISSED), threePointAttend - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(0), points - 3);

                SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,
                          SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0) - 3);

                break;

            case Constants.RecordDataType.OFFENSIVE_FOUL:

                int turnover = mGameInfo.getDetailData()
                          .get(quarter)
                          .get(playerNumber)
                          .get(Constants.RecordDataType.TURNOVER);

                if (turnover == 0) return;

                mGameInfo.getDetailData().get(quarter).get(playerNumber).put(Constants.RecordDataType.TURNOVER, turnover - 1);
                contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.RecordDataType.TURNOVER), turnover - 1);
                //type轉回FOUL，後續記錄至Db裡
                type = Constants.RecordDataType.FOUL;

                break;

            case Constants.RecordDataType.DEFENSIVE_FOUL:
                // 不是此節代表為歷史紀錄，已無關團犯。
                if (quarter == mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER)) {

                    int teamFoul = mGameInfo.getTeamData().get(Constants.RecordDataType.YOUR_TEAM_FOUL);
                    if (teamFoul == 0) return;

                    mGameInfo.getTeamData().put(Constants.RecordDataType.YOUR_TEAM_FOUL, teamFoul - 1);
                    SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL, teamFoul - 1);
                }
                //type轉回FOUL，後續記錄至Db裡
                type = Constants.RecordDataType.FOUL;

                break;
        }//TODO 犯滿畢業


        int dataValue = mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).get(type);

        if (dataValue == 0) return;

        mGameInfo.getDetailData()
                  .get(quarter)
                  .get(playerNumber).put(type, dataValue - 1);

        contentValues.put(Constants.COLUMN_NAME_SPARSE_ARRAY.get(type), dataValue - 1);

        int result = getWritableDatabase()
                  .update(Constants.GameDataDBContract.TABLE_NAME,
                            contentValues,
                            Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ? AND " +
                                      Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " = ? AND " +
                                      Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER + " = ?",
                            new String[]{mGameInfo.getGameId(), String.valueOf(quarter), String.valueOf(playerNumber)});

        Log.d(TAG, "result = " + result);
    }

    /**
     * Remove specific game data in GameDataDb
     *
     * @param gameId Id of specific game wants to be remove
     */
    public void removeGameData(String gameId) {

        if (!gameId.equals("")) {

            getWritableDatabase().delete(Constants.GameDataDBContract.TABLE_NAME,
                      Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ?",
                      new String[]{gameId});
        }
    }

    /**
     * Delete all game data in GameDataDb excepts not ended game save in SharedPreferences
     *
     * @param notEndedGameId Id of game saved in SharedPreferences "PLAYING_GAME"
     */
    public void deleteAll(@Nullable String notEndedGameId) {
        if (notEndedGameId == null || notEndedGameId.equals("")) {
            getWritableDatabase().delete(Constants.GameDataDBContract.TABLE_NAME, null, null);
        } else {
            getWritableDatabase().delete(Constants.GameDataDBContract.TABLE_NAME,
                      Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " !=?",
                      new String[]{notEndedGameId});
        }
    }

    @Deprecated
    public void writeGameData(int position, int type) {

//        plusData(position, type);
//
//        if (type < Constants.RecordDataType.JUDGEMENT_NUMBER){
//        }else {
//            minusData(position, type);
//        }
    }

    @Deprecated
    public void undoGameData(int position, int type) {

//        minusData(position, type);
//
//        type < 0x1000代表 原本輸入的指令為 increase, 要undo要decrease
//        if (type < Constants.RecordDataType.JUDGEMENT_NUMBER){
//            minusData(position, type);
//        }else {
//            plusData(position, type);
//        }

    }

    /**
     * Now using {@link #getHistoryStatistic(String, String[], String, String)}
     *
     * @return Cursor of gameId in mGameInfo
     */
    @Deprecated
    public Cursor getGameStatistic() {

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
                  Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ?",
                  new String[]{mGameInfo.getGameId()}, //TODO dynamic gameid  shoulb be mGameInfo.getId
                  Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, null, Constants.GameDataDBContract.COLUMN_NAME_POINTS);

        return cursor;
    }
}
