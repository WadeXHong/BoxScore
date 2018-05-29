package com.wadexhong.boxscore.firebasemodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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


    public static void test() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //gameinfo & gamedata
                for (DataSnapshot games : dataSnapshot.child(Constants.GameInfoDBContract.TABLE_NAME).getChildren()){
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

                    Log.d(TAG, "gameId =           "+gameId            );
                    Log.d(TAG, "gameDate =         "+gameDate          );
                    Log.d(TAG, "gameName =         "+gameName          );
                    Log.d(TAG, "isGameOver =       "+isGameOver        );
                    Log.d(TAG, "maxFoul =          "+maxFoul           );
                    Log.d(TAG, "opponent =         "+opponent          );
                    Log.d(TAG, "opponentTeamScore ="+opponentTeamScore );
                    Log.d(TAG, "quarterLength =    "+quarterLength     );
                    Log.d(TAG, "timeoutFirstHalf = "+timeoutFirstHalf  );
                    Log.d(TAG, "timeoutSecondHalf ="+timeoutSecondHalf );
                    Log.d(TAG, "totalQuarter =     "+totalQuarter      );
                    Log.d(TAG, "yourTeam =         "+yourTeam          );
                    Log.d(TAG, "yourTeamId =       "+yourTeamId        );
                    Log.d(TAG, "yourTeamScore =    "+yourTeamScore     );


                    //TODO  DO  SOMETHING IN GAMEINFODATABASE

                    // games = List<gameUUID>
                    for (DataSnapshot players : games.child(Constants.GameDataDBContract.TABLE_NAME).getChildren()){
                        //other info includes player_name, player_number

                        String playerId =              players.getKey();
                        String playerName =   (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).getValue();
                        String playerNumber = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).getValue();

                        Log.d(playerId, "playerId "+playerId);
                        Log.d(playerId, "playerName "+playerName);
                        Log.d(playerId, "playerNumber "+playerNumber);



                        //players = List<playerUUID>
                        for(DataSnapshot quarters:players.child(Constants.GameDataDBContract.COLUMN_NAME_QUARTER).getChildren()){

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

                            Log.d(TAG, "quarter "+quarter);
                            Log.d(TAG, "PTS = " + PTS );
                            Log.d(TAG, "FGM = " + FGM );
                            Log.d(TAG, "FGA = " + FGA );
                            Log.d(TAG, "TPM = " + TPM );
                            Log.d(TAG, "TPA = " + TPA );
                            Log.d(TAG, "FTM = " + FTM );
                            Log.d(TAG, "FTA = " + FTA );
                            Log.d(TAG, "OREB = " + OREB);
                            Log.d(TAG, "DREB = " + DREB);
                            Log.d(TAG, "AST = " + AST );
                            Log.d(TAG, "STL = " + STL );
                            Log.d(TAG, "BLK = " + BLK );
                            Log.d(TAG, "PF = " + PF);
                            Log.d(TAG, "TOV = " + TOV );


                        }
                    }
                }



                //playerlist

                for (DataSnapshot players: dataSnapshot.child(Constants.FireBaseConstant.PLAYER_LIST).getChildren()){
                    String playerId =              players.getKey();
                    String playerName =   (String) players.child(Constants.TeamPlayersContract.PLAYER_NAME).getValue();
                    String playerNumber = (String) players.child(Constants.TeamPlayersContract.PLAYER_NUMBER).getValue();
                    String playC =        (String) players.child(Constants.TeamPlayersContract.PLAY_C).getValue();
                    String playPF =       (String) players.child(Constants.TeamPlayersContract.PLAY_PF).getValue();
                    String playSF =       (String) players.child(Constants.TeamPlayersContract.PLAY_SF).getValue();
                    String playSG =       (String) players.child(Constants.TeamPlayersContract.PLAY_SG).getValue();
                    String playPG =       (String) players.child(Constants.TeamPlayersContract.PLAY_PG).getValue();


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

                for (DataSnapshot teams: dataSnapshot.child(Constants.TeamInfoDBContract.TABLE_NAME).getChildren()){
                    String teamId =                 teams.getKey();
                    String teamName =      (String) teams.child(Constants.TeamInfoDBContract.TEAM_NAME).getValue();
                    long   historyAmount = (long)   teams.child(Constants.TeamInfoDBContract.TEAM_NAME).getValue();
                    long   playersAmount = (long)   teams.child(Constants.TeamInfoDBContract.TEAM_NAME).getValue();
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
//                        Log.d("key", snap.getKey());
//                        Log.d("value", (String) snap.getValue());
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
//                        Log.d("key", snap.getKey());
//
//                        for (DataSnapshot snapChild:snap.child("players").getChildren()){
//                            Log.d("kk", snapChild.getKey());
//                        }
//
////                        Log.d("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });