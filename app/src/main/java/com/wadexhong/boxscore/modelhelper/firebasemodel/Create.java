package com.wadexhong.boxscore.modelhelper.firebasemodel;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/29.
 */

public class Create {

    private static Create mInstance;

    private Create(){}

    public static Create getInstance(){
        if (mInstance == null) mInstance = new Create();
            return mInstance;
    }


    public void CreateTeam(String teamId, String teamName){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.TEAM_INFO).child(teamId);
        ref.child(Constants.TeamInfoDBContract.TEAM_NAME).setValue(teamName);
        ref.child(Constants.TeamInfoDBContract.TEAM_PLAYERS_AMOUNT).setValue(0);
        ref.child(Constants.TeamInfoDBContract.TEAM_HISTORY_AMOUNT).setValue(0);

    }

    public static void test() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //gameinfo & gamedata
                for (DataSnapshot games : dataSnapshot.child(Constants.FireBaseConstant.GAME_INFO).getChildren()){
                    //other info includes game_data, game_name....

                    String  gameId =                      games.getKey();
                    String  gameDate =           (String) games.child(Constants.GameInfoDBContract.GAME_DATE).getValue();
                    String  gameName =           (String) games.child(Constants.GameInfoDBContract.GAME_NAME).getValue();
                    boolean isGameOver =         (boolean)games.child(Constants.GameInfoDBContract.IS_GAMEOVER).getValue();
                    long    maxFoul =            (long)   games.child(Constants.GameInfoDBContract.MAX_FOUL).getValue();
                    String  opponent =           (String) games.child(Constants.GameInfoDBContract.OPPONENT_NAME).getValue();
                    long    opponentTeamScore =  (long)   games.child(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE).getValue();
                    long    quarterLength =      (long)   games.child(Constants.GameInfoDBContract.QUARTER_LENGTH).getValue();
                    long    timeoutFirstHalf =   (long)   games.child(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF).getValue();
                    long    timeoutSecondHalf =  (long)   games.child(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF).getValue();
                    long    totalQuarter =       (long)   games.child(Constants.GameInfoDBContract.TOTAL_QUARTER).getValue();
                    String  yourTeam =           (String) games.child(Constants.GameInfoDBContract.YOUR_TEAM).getValue();
                    String  yourTeamId =         (String) games.child(Constants.GameInfoDBContract.YOUR_TEAM_ID).getValue();
                    long    yourTeamScore =      (long)   games.child(Constants.GameInfoDBContract.YOUR_TEAM_SCORE).getValue();

                    Log.w(gameId, "gameId =           "+gameId            );
                    Log.w(gameId, "gameDate =         "+gameDate          );
                    Log.w(gameId, "gameName =         "+gameName          );
                    Log.w(gameId, "isGameOver =       "+isGameOver        );
                    Log.w(gameId, "maxFoul =          "+maxFoul           );
                    Log.w(gameId, "opponent =         "+opponent          );
                    Log.w(gameId, "opponentTeamScore ="+opponentTeamScore );
                    Log.w(gameId, "quarterLength =    "+quarterLength     );
                    Log.w(gameId, "timeoutFirstHalf = "+timeoutFirstHalf  );
                    Log.w(gameId, "timeoutSecondHalf ="+timeoutSecondHalf );
                    Log.w(gameId, "totalQuarter =     "+totalQuarter      );
                    Log.w(gameId, "yourTeam =         "+yourTeam          );
                    Log.w(gameId, "yourTeamId =       "+yourTeamId        );
                    Log.w(gameId, "yourTeamScore =    "+yourTeamScore     );


                    //TODO  DO  SOMETHING IN GAMEINFODATABASE

                    // games = List<gameUUID>
                    for (DataSnapshot players : games.child(Constants.FireBaseConstant.GAME_DATA).getChildren()){
                        //other info includes player_name, player_number

                        String playerId =              players.getKey();
                        String playerName =   (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).getValue();
                        String playerNumber = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).getValue();

                        Log.w(playerId, "playerId "+playerId);
                        Log.w(playerId, "playerName "+playerName);
                        Log.w(playerId, "playerNumber "+playerNumber);



                        //players = List<playerUUID>
                        for(DataSnapshot quarters:players.child(Constants.FireBaseConstant.QUARTER).getChildren()){

                            String quarter = quarters.getKey();

                            long PTS =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_POINTS).getValue();
                            long FGM =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE).getValue();
                            long FGA =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED).getValue();
                            long TPM =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE).getValue();
                            long TPA =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED).getValue();
                            long FTM =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE).getValue();
                            long FTA =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED).getValue();
                            long OREB = (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND ).getValue();
                            long DREB = (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND ).getValue();
                            long AST =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_ASSIST).getValue();
                            long STL =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_STEAL).getValue();
                            long BLK =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_BLOCK).getValue();
                            long PF =   (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL).getValue();
                            long TOV =  (long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER).getValue();

                            Log.w(quarter, "quarter "+quarter);
                            Log.w(quarter, "PTS = " + PTS );
                            Log.w(quarter, "FGM = " + FGM );
                            Log.w(quarter, "FGA = " + FGA );
                            Log.w(quarter, "TPM = " + TPM );
                            Log.w(quarter, "TPA = " + TPA );
                            Log.w(quarter, "FTM = " + FTM );
                            Log.w(quarter, "FTA = " + FTA );
                            Log.w(quarter, "OREB = " + OREB);
                            Log.w(quarter, "DREB = " + DREB);
                            Log.w(quarter, "AST = " + AST );
                            Log.w(quarter, "STL = " + STL );
                            Log.w(quarter, "BLK = " + BLK );
                            Log.w(quarter, "PF = " + PF);
                            Log.w(quarter, "TOV = " + TOV );


                        }
                    }
                }



                //playerlist

                //teamlist


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void CreateGameData (Cursor cursor){

        cursor.moveToFirst();
        int size = cursor.getCount();
        String gameId = cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID));


        for (int i = 0 ; i < size ; i++) {

            cursor.moveToPosition(i);

            int quarter = cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_QUARTER));
            String playerId = cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID));
            String playerNumber = cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER));
            String playerName = cursor.getString(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME));
            int PTS =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS));
            int FGM =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE));
            int FGA =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED));
            int TPM =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE));
            int TPA =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED));
            int FTM =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE));
            int FTA =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED));
            int OREB = cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND));
            int DREB = cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND));
            int AST =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_ASSIST));
            int STL =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_STEAL));
            int BLK =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_BLOCK));
            int PF =   cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL));
            int TOV =  cursor.getInt(cursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.USERS)
                      .child(FirebaseAuth.getInstance().getUid())
                      .child(Constants.FireBaseConstant.GAME_INFO)
                      .child(gameId)
                      .child(Constants.FireBaseConstant.GAME_DATA)
                      .child(playerId);
            ref.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).setValue(playerName);
            ref.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).setValue(playerNumber);

            DatabaseReference data = ref.child(Constants.GameDataDBContract.COLUMN_NAME_QUARTER).child(String.valueOf(quarter));
            data.child(Constants.GameDataDBContract.COLUMN_NAME_POINTS).setValue(PTS);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE).setValue(FGM);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED).setValue(FGA);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE).setValue(TPM);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED).setValue(TPA);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE).setValue(FTM);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED).setValue(FTA);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND).setValue(OREB);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND).setValue(DREB);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_ASSIST).setValue(AST);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_STEAL).setValue(STL);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_BLOCK).setValue(BLK);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL).setValue(PF);
            data.child(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER).setValue(TOV);

        }
    }

    public void CreateGameInfo (Cursor cursor){

        cursor.moveToFirst();
        String gameId            = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.GAME_ID));
        String gameData          = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.GAME_DATE));
        String gameName          = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.GAME_NAME));
        int    isGameOver        = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.IS_GAMEOVER));
        int    maxFoul           = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.MAX_FOUL));
        String opponent          = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_NAME));
        int    opponentScore     = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE));
        int    quarterLength     = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.QUARTER_LENGTH));
        String team              = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM));
        int    timeoutFirstHalf  = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF));
        int    timeoutSecondHalf = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF));
        int    totalQuarter      = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.TOTAL_QUARTER));
        int    yourScore         = cursor.getInt   (cursor.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM_SCORE));
        String teamId            = cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM_ID));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.GAME_INFO).child(gameId);

        ref.child(Constants.GameInfoDBContract.GAME_ID).setValue(gameId);
        ref.child(Constants.GameInfoDBContract.GAME_DATE).setValue(gameData);
        ref.child(Constants.GameInfoDBContract.GAME_NAME).setValue(gameName);
        ref.child(Constants.GameInfoDBContract.IS_GAMEOVER).setValue(isGameOver);
        ref.child(Constants.GameInfoDBContract.MAX_FOUL).setValue(maxFoul);
        ref.child(Constants.GameInfoDBContract.OPPONENT_NAME).setValue(opponent);
        ref.child(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE).setValue(opponentScore);
        ref.child(Constants.GameInfoDBContract.QUARTER_LENGTH).setValue(quarterLength);
        ref.child(Constants.GameInfoDBContract.YOUR_TEAM).setValue(team);
        ref.child(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF).setValue(timeoutFirstHalf);
        ref.child(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF).setValue(timeoutSecondHalf);
        ref.child(Constants.GameInfoDBContract.TOTAL_QUARTER).setValue(totalQuarter);
        ref.child(Constants.GameInfoDBContract.YOUR_TEAM_SCORE).setValue(yourScore);
        ref.child(Constants.GameInfoDBContract.YOUR_TEAM_ID).setValue(teamId);
    }

    public void CreatePlayer(String teamId, Player player, int newPlayerAmount){

        DatabaseReference refPlayer = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.TEAM_PLAYER).child(player.getPlayerId());
        refPlayer.child(Constants.TeamPlayersContract.PLAYER_NAME).setValue(player.getName());
        refPlayer.child(Constants.TeamPlayersContract.PLAYER_NUMBER).setValue(player.getNumber());
        refPlayer.child(Constants.TeamPlayersContract.PLAY_C).setValue(player.getPosition()[Player.POSITION_C]);
        refPlayer.child(Constants.TeamPlayersContract.PLAY_PF).setValue(player.getPosition()[Player.POSITION_PF]);
        refPlayer.child(Constants.TeamPlayersContract.PLAY_SF).setValue(player.getPosition()[Player.POSITION_SF]);
        refPlayer.child(Constants.TeamPlayersContract.PLAY_SG).setValue(player.getPosition()[Player.POSITION_SG]);
        refPlayer.child(Constants.TeamPlayersContract.PLAY_PG).setValue(player.getPosition()[Player.POSITION_PG]);
        refPlayer.child(Constants.TeamPlayersContract.TEAM_ID).setValue(teamId);

        UpdateTeamPlayersAmount(teamId, newPlayerAmount);
    }

    public void UpdateTeamPlayersAmount(String teamId, int newPlayerAmount) {
        DatabaseReference refTeamInfo = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.TEAM_INFO).child(teamId).child(Constants.TeamInfoDBContract.TEAM_PLAYERS_AMOUNT);
        refTeamInfo.setValue(newPlayerAmount);
    }

    public void UpdateTeamHistoryAmount(String teamId, int newHistoryAmount) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.TEAM_INFO).child(teamId).child(Constants.TeamInfoDBContract.TEAM_HISTORY_AMOUNT);
        ref.setValue(newHistoryAmount);
    }
}



//
//            DatabaseReference refTest = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("teamList");
//            refTest.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
//                        Log.w("key", snap.getKey());
//                        Log.w("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });



//            DatabaseReference refTest = FirebaseDatabase.getInstance().getReference("teams").child(FirebaseAuth.getInstance().getUid());
//            refTest.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
//                        Log.w("key", snap.getKey());
//
//                        for (DataSnapshot snapChild:snap.child("players").getChildren()){
//                            Log.w("kk", snapChild.getKey());
//                        }
//
////                        Log.w("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });