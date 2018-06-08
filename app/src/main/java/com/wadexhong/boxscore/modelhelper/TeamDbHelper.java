package com.wadexhong.boxscore.modelhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Create;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Remove;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.TeamInfo;
import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.CreatePlayerFragment;
import com.wadexhong.boxscore.objects.TeamDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TeamDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "teamInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE_TEAMINFO =
              "CREATE TABLE " + Constants.TeamInfoDBContract.TABLE_NAME + "(" +
                        Constants.TeamInfoDBContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME + " TEXT NOT NULL, " +
                        Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " TEXT, " +
                        Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT + " INTEGER DEFAULT 0, " +
                        Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT + " INTEGER DEFAULT 0" + ");";

    public static final String SQL_CREATE_TEAMPLAYER =
              "CREATE TABLE " + Constants.TeamPlayersContract.TABLE_NAME + "(" +
                        Constants.TeamPlayersContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER + " TEXT NOT NULL, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAY_C + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PF + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SF + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SG + " BOOLEAN DEFAULT 0, " +
                        Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PG + " BOOLEAN DEFAULT 0" + ");";


    public TeamDbHelper(Context context) {
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

    public boolean createTeamInDB(String teamName) {

        Cursor cursor = getReadableDatabase().query(Constants.TeamInfoDBContract.TABLE_NAME,
                  new String[]{Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME},
                  Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME + " =?",
                  new String[]{teamName}, null, null, null);

        if (cursor.getCount() == 0) {

            String teamUuid = UUID.randomUUID().toString();
            //Local SQLite
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME, teamName);
            contentValues.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID, teamUuid);
            // TODO 未來新增球隊的輸入資料放在這

            getWritableDatabase().insert(Constants.TeamInfoDBContract.TABLE_NAME, null, contentValues);
            getWritableDatabase().close();

            //add to firebase;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Create.getInstance().createTeam(teamUuid, teamName);
            }
            return true;

        } else {
            return false;
        }
    }

    public void createPlayerInDb(String teamId, Player player) {

        String playerId = UUID.randomUUID().toString();
        player.setPlayerId(playerId);

        ContentValues cvPlayers = new ContentValues();

        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID, playerId);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER, player.getNumber());
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME, player.getName());
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_C, player.getPosition()[CreatePlayerFragment.POSITION_C]);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PF, player.getPosition()[CreatePlayerFragment.POSITION_PF]);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SF, player.getPosition()[CreatePlayerFragment.POSITION_SF]);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SG, player.getPosition()[CreatePlayerFragment.POSITION_SG]);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PG, player.getPosition()[CreatePlayerFragment.POSITION_PG]);
        cvPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID, teamId);

        getWritableDatabase().insert(Constants.TeamPlayersContract.TABLE_NAME, null, cvPlayers);

        ContentValues cvInfo = new ContentValues();
        Cursor cursor = getReadableDatabase().query(Constants.TeamInfoDBContract.TABLE_NAME,
                  new String[]{Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT},
                  Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?",
                  new String[]{teamId}, null, null, null);

        cursor.moveToFirst();

        int teamPlayerAmount = cursor.getInt(0);
        cvInfo.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT, teamPlayerAmount + 1);

        getWritableDatabase().update(Constants.TeamInfoDBContract.TABLE_NAME, cvInfo, Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?", new String[]{teamId});

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Create.getInstance().createPlayer(teamId, player, teamPlayerAmount + 1);
        }
    }

    public ArrayList<TeamInfo> getTeamsForAdapter() {

        Cursor cursor = getReadableDatabase().query(Constants.TeamInfoDBContract.TABLE_NAME, null, null, null, null, null, null);
        int size = cursor.getCount();
        ArrayList<TeamInfo> teamInfoList = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            cursor.moveToPosition(i);
            String teamName = cursor.getString(cursor.getColumnIndex(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME));
            String teamId = cursor.getString(cursor.getColumnIndex(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID));
            int teamPlayerAmount = cursor.getInt(cursor.getColumnIndex(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT));
            int teamHistoryAmount = cursor.getInt(cursor.getColumnIndex(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT));

            List<TeamDetail> teamDetails = new ArrayList<>();
            teamDetails.add(new TeamDetail(teamName, teamId, teamPlayerAmount, teamHistoryAmount));

            teamInfoList.add(new TeamInfo(teamName, teamId, teamDetails));
        }

        return teamInfoList;
    }

    public Cursor getPlayersFromDb(String teamId) {
        Log.d(TAG, "getPlayersFromDb executed, teamId = " + teamId);
        return getWritableDatabase().query(Constants.TeamPlayersContract.TABLE_NAME, null,
                  Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID + " =?", new String[]{teamId}, null, null, null);
    }

    public void updateHistoryAmount(String teamId, int amount) {

        ContentValues cv = new ContentValues();
        cv.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT, amount);

        getWritableDatabase().update(Constants.TeamInfoDBContract.TABLE_NAME,
                  cv,
                  Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?",
                  new String[]{teamId});
    }

    public boolean checkNumberIsExistInDb(String teamId, String playerNumber) {
        Log.d(TAG, "checkNumberIsExistInDb executed, teamId = " + teamId);
        int size = getReadableDatabase().query(Constants.TeamPlayersContract.TABLE_NAME, null,
                  Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID + " =? AND " + Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER + " =?",
                  new String[]{teamId, playerNumber}, null, null, null).getCount();

        return size != 0;
    }

    public void deletePlayerInDb(String teamId, String playerId) {

        int result = getWritableDatabase().delete(Constants.TeamPlayersContract.TABLE_NAME, Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID + " =?", new String[]{playerId});
        Log.d(TAG, "deletePlayerInDb result : " + result);

        if (result > 0) {

            Cursor cursor = getReadableDatabase().query(Constants.TeamInfoDBContract.TABLE_NAME,
                      null,
                      Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?",
                      new String[]{teamId},
                      null, null, null, null);

            if (cursor.getCount() == 1) {

                cursor.moveToFirst();
                int playerAmount = cursor.getInt(cursor.getColumnIndex(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT));

                ContentValues cv = new ContentValues();
                cv.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT, playerAmount - 1);

                getWritableDatabase().update(Constants.TeamInfoDBContract.TABLE_NAME, cv, Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?", new String[]{teamId});
                Remove.getInstance().removePlayer(teamId, playerId, playerAmount - 1);
            }
        }
    }

    public void deleteTeamInDb(String teamId) {
        getWritableDatabase().delete(Constants.TeamInfoDBContract.TABLE_NAME, Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID + " =?", new String[]{teamId});
        getWritableDatabase().delete(Constants.TeamPlayersContract.TABLE_NAME, Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID + " =?", new String[]{teamId});
    }

    public void deleteAll() {
        getWritableDatabase().delete(Constants.TeamInfoDBContract.TABLE_NAME, null, null);
        getWritableDatabase().delete(Constants.TeamPlayersContract.TABLE_NAME, null, null);
    }
}
