package com.wadexhong.boxscore.firebasemodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wadexhong.boxscore.Constants;

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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.TEAM_LIST).child(teamId);
        ref.child(Constants.TeamInfoDBContract.TABLE_NAME).setValue(teamName);
        ref.child(Constants.TeamInfoDBContract.TEAM_PLAYERS_AMOUNT).setValue(0);
        ref.child(Constants.TeamInfoDBContract.TEAM_HISTORY_AMOUNT).setValue(0);

        test();
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
                    for (DataSnapshot players : games.child(Constants.GameDataDBContract.TABLE_NAME).getChildren()){
                        //other info includes player_name, player_number

                        String playerId =              players.getKey();
                        String playerName =   (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME).getValue();
                        String playerNumber = (String) players.child(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER).getValue();

                        Log.w(playerId, "playerId "+playerId);
                        Log.w(playerId, "playerName "+playerName);
                        Log.w(playerId, "playerNumber "+playerNumber);



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