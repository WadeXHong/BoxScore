package com.bardxhong.boxscore.modelhelper.firebasemodel;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bardxhong.boxscore.Constants;

/**
 * Created by wade8 on 2018/6/8.
 */

public class Delete {

    private final String TAG = Delete.class.getSimpleName();
    private static Delete mInstance;
    private static DatabaseReference mReference;

    public static Delete getInstance() {
        if (mInstance == null) mInstance = new Delete();
        mReference = FirebaseDatabase.getInstance().getReference()
                  .child(Constants.FireBaseConstant.NODE_NAME_USERS)
                  .child(FirebaseAuth.getInstance().getUid());
        return mInstance;
    }

    public void deletePlayer(String teamId, String playerId, int newPlayerAmount) {

        mReference.child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER)
                  .child(playerId)
                  .removeValue();

        updateTeamPlayersAmount(teamId, newPlayerAmount);
    }

    public void deleteTeam(String teamId) {

        mReference.child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO).child(teamId).removeValue();

        mReference.child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER)
                  .orderByChild(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID)
                  .equalTo(teamId)
                  .addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          for (DataSnapshot players : dataSnapshot.getChildren()) {
                              players.getRef().removeValue();
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });
    }

    public void deleteGame(String teamId, String gameId, int newHistoryAmount) {

        mReference.child(Constants.FireBaseConstant.NODE_NAME_GAME_INFO)
                  .child(gameId)
                  .removeValue();

        updateTeamHistoryAmount(teamId, newHistoryAmount);
    }

    private void updateTeamPlayersAmount(String teamId, int newPlayerAmount) {

        mReference.child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO)
                  .child(teamId).child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT)
                  .setValue(newPlayerAmount);
    }

    private void updateTeamHistoryAmount(String teamId, int newHistoryAmount) {

        mReference.child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO)
                  .child(teamId).child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT)
                  .setValue(newHistoryAmount);
    }
}
