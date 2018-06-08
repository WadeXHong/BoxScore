package com.wadexhong.boxscore.modelhelper.firebasemodel;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wadexhong.boxscore.Constants;

/**
 * Created by wade8 on 2018/6/8.
 */

public class Remove {

    private final String TAG = Remove.class.getSimpleName();
    private static Remove mInstance;

    public static Remove getInstance() {
        if (mInstance == null) mInstance = new Remove();
        return mInstance;
    }

    public void removePlayer(String teamId, String playerId, int newPlayerAmount) {

        DatabaseReference refPlayer = FirebaseDatabase.getInstance().getReference()
                  .child(Constants.FireBaseConstant.NODE_NAME_USERS)
                  .child(FirebaseAuth.getInstance().getUid())
                  .child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER)
                  .child(playerId);

        refPlayer.removeValue();
        updateTeamPlayersAmount(teamId, newPlayerAmount);
    }

    public void removeTeam(String teamId) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                  .child(Constants.FireBaseConstant.NODE_NAME_USERS)
                  .child(FirebaseAuth.getInstance().getUid());

        ref.child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO).child(teamId).removeValue();

        ref.child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER)
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

    public void updateTeamPlayersAmount(String teamId, int newPlayerAmount) {
        DatabaseReference refTeamInfo = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.NODE_NAME_USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO).child(teamId).child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT);
        refTeamInfo.setValue(newPlayerAmount);
    }
}
