package com.wadexhong.boxscore.modelhelper.task;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.dialog.ProgressBarDialog;

/**
 * Created by wade8 on 2018/5/31.
 * Put database synchronize process into this asynctask.
 */

public class OnCreateDataBaseTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = OnCreateDataBaseTask.class.getSimpleName();

    private DataSnapshot mDataSnapshot;

    public OnCreateDataBaseTask(DataSnapshot dataSnapshot) {
        super();

        mDataSnapshot = dataSnapshot;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        SQLiteDatabase dbGameInfo = BoxScore.getGameInfoDbHelper().getWritableDatabase();
        SQLiteDatabase dbGameData = BoxScore.getGameDataDbHelper().getWritableDatabase();
        SQLiteDatabase dbTeam = BoxScore.getTeamDbHelper().getReadableDatabase();


        //gameinfo & gamedata
        for (DataSnapshot games : mDataSnapshot.child(Constants.FireBaseConstant.NODE_NAME_GAME_INFO).getChildren()) {
            //other info includes game_data, game_name....

            String gameId = games.getKey();
            String gameDate = (String) games.child(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE).getValue();
            String gameName = (String) games.child(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME).getValue();
            Long isGameOver = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER).getValue();
            Long maxFoul = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL).getValue();
            String opponent = (String) games.child(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME).getValue();
            Long opponentTeamScore = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE).getValue();
            Long quarterLength = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH).getValue();
            Long timeoutFirstHalf = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF).getValue();
            Long timeoutSecondHalf = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF).getValue();
            Long totalQuarter = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER).getValue();
            String yourTeam = (String) games.child(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM).getValue();
            String yourTeamId = (String) games.child(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID).getValue();
            Long yourTeamScore = (Long) games.child(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE).getValue();

            Log.d(TAG, "game_info");
            Log.i(TAG, "gameId =           " + gameId);
            Log.i(TAG, "gameDate =         " + gameDate);
            Log.i(TAG, "gameName =         " + gameName);
            Log.i(TAG, "isGameOver =       " + isGameOver);
            Log.i(TAG, "maxFoul =          " + maxFoul);
            Log.i(TAG, "opponent =         " + opponent);
            Log.i(TAG, "opponentTeamScore =" + opponentTeamScore);
            Log.i(TAG, "quarterLength =    " + quarterLength);
            Log.i(TAG, "timeoutFirstHalf = " + timeoutFirstHalf);
            Log.i(TAG, "timeoutSecondHalf =" + timeoutSecondHalf);
            Log.i(TAG, "totalQuarter =     " + totalQuarter);
            Log.i(TAG, "yourTeam =         " + yourTeam);
            Log.i(TAG, "yourTeamId =       " + yourTeamId);
            Log.i(TAG, "yourTeamScore =    " + yourTeamScore);


            //TODO  DO  SOMETHING IN GAMEINFODATABASE

            ContentValues cvGameInfo = new ContentValues();
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID, gameId);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE, gameDate);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME, gameName);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER, isGameOver);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL, maxFoul);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME, opponent);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE, opponentTeamScore);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH, quarterLength);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF, timeoutFirstHalf);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF, timeoutSecondHalf);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER, totalQuarter);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM, yourTeam);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID, yourTeamId);
            cvGameInfo.put(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE, yourTeamScore);

            dbGameInfo.insert(Constants.GameInfoDBContract.TABLE_NAME, null, cvGameInfo);


            // games = List<gameUUID>
            for (DataSnapshot players : games.child(Constants.FireBaseConstant.NODE_NAME_GAME_DATA).getChildren()) {
                //other info includes player_name, player_number

                String playerId = players.getKey();
                String playerName = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).getValue();
                String playerNumber = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).getValue();

                Log.d(TAG, "game_data");
                Log.i(TAG, "playerId " + playerId);
                Log.i(TAG, "playerName " + playerName);
                Log.i(TAG, "playerNumber " + playerNumber);


                //players = List<playerUUID>
                for (DataSnapshot quarters : players.child(Constants.FireBaseConstant.NODE_NAME_QUARTER).getChildren()) {

                    String quarter = quarters.getKey();

                    Long PTS = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_POINTS).getValue();
                    Long FGM = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE).getValue();
                    Long FGA = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED).getValue();
                    Long TPM = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE).getValue();
                    Long TPA = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED).getValue();
                    Long FTM = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE).getValue();
                    Long FTA = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED).getValue();
                    Long OREB = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND).getValue();
                    Long DREB = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND).getValue();
                    Long AST = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_ASSIST).getValue();
                    Long STL = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_STEAL).getValue();
                    Long BLK = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_BLOCK).getValue();
                    Long PF = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL).getValue();
                    Long TOV = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER).getValue();

                    Log.i(TAG, "quarter " + quarter);
                    Log.i(TAG, "PTS = " + PTS);
                    Log.i(TAG, "FGM = " + FGM);
                    Log.i(TAG, "FGA = " + FGA);
                    Log.i(TAG, "TPM = " + TPM);
                    Log.i(TAG, "TPA = " + TPA);
                    Log.i(TAG, "FTM = " + FTM);
                    Log.i(TAG, "FTA = " + FTA);
                    Log.i(TAG, "OREB = " + OREB);
                    Log.i(TAG, "DREB = " + DREB);
                    Log.i(TAG, "AST = " + AST);
                    Log.i(TAG, "STL = " + STL);
                    Log.i(TAG, "BLK = " + BLK);
                    Log.i(TAG, "PF = " + PF);
                    Log.i(TAG, "TOV = " + TOV);

                    ContentValues cvGameData = new ContentValues();

                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, gameId);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_QUARTER, quarter);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID, playerId);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, playerNumber);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, playerName);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_POINTS, PTS);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE, FGM);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED, FGA);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE, TPM);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED, TPA);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE, FTM);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED, FTA);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND, OREB);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND, DREB);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_ASSIST, AST);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_STEAL, STL);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_BLOCK, BLK);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL, PF);
                    cvGameData.put(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER, TOV);

                    dbGameData.insert(Constants.GameDataDBContract.TABLE_NAME, null, cvGameData);
                }
            }
        }


        //playerlist

        for (DataSnapshot players : mDataSnapshot.child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER).getChildren()) {
            String playerId = players.getKey();
            String teamId = (String) players.child(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID).getValue();
            String playerName = (String) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME).getValue();
            String playerNumber = (String) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER).getValue();
            Long playC = (Long) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_C).getValue();
            Long playPF = (Long) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PF).getValue();
            Long playSF = (Long) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SF).getValue();
            Long playSG = (Long) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SG).getValue();
            Long playPG = (Long) players.child(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PG).getValue();

            Log.d(TAG, "team_player");
            Log.i(TAG, "playerId    " + playerId);
            Log.i(TAG, "teamId      " + teamId);
            Log.i(TAG, "playerName  " + playerName);
            Log.i(TAG, "playerNumber" + playerNumber);
            Log.i(TAG, "playC =     " + playC);
            Log.i(TAG, "playPF =    " + playPF);
            Log.i(TAG, "playSF =    " + playSF);
            Log.i(TAG, "playSG =    " + playSG);
            Log.i(TAG, "playPG =    " + playPG);

            ContentValues cvTeamPlayers = new ContentValues();

            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID, teamId);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID, playerId);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME, playerName);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER, playerNumber);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_C, playC);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PF, playPF);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SF, playSF);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SG, playSG);
            cvTeamPlayers.put(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PG, playPG);

            dbTeam.insert(Constants.TeamPlayersContract.TABLE_NAME, null, cvTeamPlayers);
        }

        //teamlist

        for (DataSnapshot teams : mDataSnapshot.child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO).getChildren()) {
            String teamId = teams.getKey();
            String teamName = (String) teams.child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME).getValue();
            Long historyAmount = (Long) teams.child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT).getValue();
            Long playersAmount = (Long) teams.child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT).getValue();

            Log.d(TAG, "team_info");
            Log.i(TAG, "teamId        " + teamId);
            Log.i(TAG, "teamName      " + teamName);
            Log.i(TAG, "historyAmount " + historyAmount);
            Log.i(TAG, "playersAmount " + playersAmount);

            ContentValues cvTeamInfo = new ContentValues();

            cvTeamInfo.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID, teamId);
            cvTeamInfo.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME, teamName);
            cvTeamInfo.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT, historyAmount);
            cvTeamInfo.put(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT, playersAmount);

            dbTeam.insert(Constants.TeamInfoDBContract.TABLE_NAME, null, cvTeamInfo);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProgressBarDialog.hideProgressBarDialog();
    }
}
