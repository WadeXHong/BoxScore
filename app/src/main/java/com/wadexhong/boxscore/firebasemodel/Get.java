package com.wadexhong.boxscore.firebasemodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wadexhong.boxscore.Constants;

/**
 * Created by wade8 on 2018/5/29.
 */

public class Get {
    private static final String TAG = Get.class.getSimpleName();

    private static Get mInstance;

    private Get(){}

    public static Get getInstance(){
        if (mInstance == null) mInstance = new Get();
            return mInstance;
    }


    public static void onCreate() {
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
                    Long    isGameOver =         (Long)   games.child(Constants.GameInfoDBContract.IS_GAMEOVER).getValue();
                    Long    maxFoul =            (Long)   games.child(Constants.GameInfoDBContract.MAX_FOUL).getValue();
                    String  opponent =           (String) games.child(Constants.GameInfoDBContract.OPPONENT_NAME).getValue();
                    Long    opponentTeamScore =  (Long)   games.child(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE).getValue();
                    Long    quarterLength =      (Long)   games.child(Constants.GameInfoDBContract.QUARTER_LENGTH).getValue();
                    Long    timeoutFirstHalf =   (Long)   games.child(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF).getValue();
                    Long    timeoutSecondHalf =  (Long)   games.child(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF).getValue();
                    Long    totalQuarter =       (Long)   games.child(Constants.GameInfoDBContract.TOTAL_QUARTER).getValue();
                    String  yourTeam =           (String) games.child(Constants.GameInfoDBContract.YOUR_TEAM).getValue();
                    String  yourTeamId =         (String) games.child(Constants.GameInfoDBContract.YOUR_TEAM_ID).getValue();
                    Long    yourTeamScore =      (Long)   games.child(Constants.GameInfoDBContract.YOUR_TEAM_SCORE).getValue();

                    Log.d(TAG, "game_info");
                    Log.i(TAG, "gameId =           "+gameId            );
                    Log.i(TAG, "gameDate =         "+gameDate          );
                    Log.i(TAG, "gameName =         "+gameName          );
                    Log.i(TAG, "isGameOver =       "+isGameOver        );
                    Log.i(TAG, "maxFoul =          "+maxFoul           );
                    Log.i(TAG, "opponent =         "+opponent          );
                    Log.i(TAG, "opponentTeamScore ="+opponentTeamScore );
                    Log.i(TAG, "quarterLength =    "+quarterLength     );
                    Log.i(TAG, "timeoutFirstHalf = "+timeoutFirstHalf  );
                    Log.i(TAG, "timeoutSecondHalf ="+timeoutSecondHalf );
                    Log.i(TAG, "totalQuarter =     "+totalQuarter      );
                    Log.i(TAG, "yourTeam =         "+yourTeam          );
                    Log.i(TAG, "yourTeamId =       "+yourTeamId        );
                    Log.i(TAG, "yourTeamScore =    "+yourTeamScore     );


                    //TODO  DO  SOMETHING IN GAMEINFODATABASE

                    // games = List<gameUUID>
                    for (DataSnapshot players : games.child(Constants.FireBaseConstant.GAME_DATA).getChildren()){
                        //other info includes player_name, player_number

                        String playerId =              players.getKey();
                        String playerName =   (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).getValue();
                        String playerNumber = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).getValue();

                        Log.d(TAG, "game_data");
                        Log.i(TAG, "playerId "+playerId);
                        Log.i(TAG, "playerName "+playerName);
                        Log.i(TAG, "playerNumber "+playerNumber);



                        //players = List<playerUUID>
                        for(DataSnapshot quarters:players.child(Constants.FireBaseConstant.QUARTER).getChildren()){

                            String quarter = quarters.getKey();

                            Long PTS =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_POINTS).getValue();
                            Long FGM =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE).getValue();
                            Long FGA =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED).getValue();
                            Long TPM =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE).getValue();
                            Long TPA =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED).getValue();
                            Long FTM =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE).getValue();
                            Long FTA =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED).getValue();
                            Long OREB = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND ).getValue();
                            Long DREB = (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND ).getValue();
                            Long AST =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_ASSIST).getValue();
                            Long STL =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_STEAL).getValue();
                            Long BLK =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_BLOCK).getValue();
                            Long PF =   (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL).getValue();
                            Long TOV =  (Long) quarters.child(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER).getValue();

                            Log.i(TAG, "quarter "+quarter);
                            Log.i(TAG, "PTS = " + PTS );
                            Log.i(TAG, "FGM = " + FGM );
                            Log.i(TAG, "FGA = " + FGA );
                            Log.i(TAG, "TPM = " + TPM );
                            Log.i(TAG, "TPA = " + TPA );
                            Log.i(TAG, "FTM = " + FTM );
                            Log.i(TAG, "FTA = " + FTA );
                            Log.i(TAG, "OREB = " + OREB);
                            Log.i(TAG, "DREB = " + DREB);
                            Log.i(TAG, "AST = " + AST );
                            Log.i(TAG, "STL = " + STL );
                            Log.i(TAG, "BLK = " + BLK );
                            Log.i(TAG, "PF = " + PF);
                            Log.i(TAG, "TOV = " + TOV );


                        }
                    }
                }



                //playerlist

                for (DataSnapshot players: dataSnapshot.child(Constants.FireBaseConstant.TEAM_PLAYER).getChildren()){
                    String playerId =              players.getKey();
                    String playerName =   (String) players.child(Constants.TeamPlayersContract.PLAYER_NAME).getValue();
                    String playerNumber = (String) players.child(Constants.TeamPlayersContract.PLAYER_NUMBER).getValue();
                    Long   playC =        (Long)   players.child(Constants.TeamPlayersContract.PLAY_C).getValue();
                    Long   playPF =       (Long)   players.child(Constants.TeamPlayersContract.PLAY_PF).getValue();
                    Long   playSF =       (Long)   players.child(Constants.TeamPlayersContract.PLAY_SF).getValue();
                    Long   playSG =       (Long)   players.child(Constants.TeamPlayersContract.PLAY_SG).getValue();
                    Long   playPG =       (Long)   players.child(Constants.TeamPlayersContract.PLAY_PG).getValue();

                    Log.d(TAG, "team_player");
                    Log.i(TAG,"playerId    " + playerId    );
                    Log.i(TAG,"playerName  " + playerName  );
                    Log.i(TAG,"playerNumber" + playerNumber);
                    Log.i(TAG,"playC =     " + playC       );
                    Log.i(TAG,"playPF =    " + playPF      );
                    Log.i(TAG,"playSF =    " + playSF      );
                    Log.i(TAG,"playSG =    " + playSG      );
                    Log.i(TAG,"playPG =    " + playPG      );
                }

                //teamlist

                for (DataSnapshot teams: dataSnapshot.child(Constants.FireBaseConstant.TEAM_INFO).getChildren()){
                    String teamId =                 teams.getKey();
                    String teamName =      (String) teams.child(Constants.TeamInfoDBContract.TEAM_NAME).getValue();
                    Long   historyAmount = (Long)   teams.child(Constants.TeamInfoDBContract.TEAM_HISTORY_AMOUNT).getValue();
                    Long   playersAmount = (Long)   teams.child(Constants.TeamInfoDBContract.TEAM_PLAYERS_AMOUNT).getValue();

                    Log.d(TAG, "team_info");
                    Log.i(TAG,"teamId        " + teamId        );
                    Log.i(TAG,"teamName      " + teamName      );
                    Log.i(TAG,"historyAmount " + historyAmount );
                    Log.i(TAG,"playersAmount " + playersAmount );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}



//
//            DatabaseReference refTest = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("teamList");
//            refTest.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
//                        Log.i("key", snap.getKey());
//                        Log.i("value", (String) snap.getValue());
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
//                        Log.i("key", snap.getKey());
//
//                        for (DataSnapshot snapChild:snap.child("players").getChildren()){
//                            Log.i("kk", snapChild.getKey());
//                        }
//
////                        Log.i("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });