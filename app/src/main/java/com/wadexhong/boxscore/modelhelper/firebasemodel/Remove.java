package com.wadexhong.boxscore.modelhelper.firebasemodel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wadexhong.boxscore.Constants;

/**
 * Created by wade8 on 2018/6/8.
 */

public class Remove {

    private final String TAG = Remove.class.getSimpleName();
    private static Remove mInstance;

    public static Remove getInstance(){
        if (mInstance == null) mInstance = new Remove();
        return mInstance;
    }

    public void removePlayer(String teamId, String playerId, int newPlayerAmount){

        DatabaseReference refPlayer = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.NODE_NAME_USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.NODE_NAME_TEAM_PLAYER).child(playerId);
        refPlayer.removeValue();

        updateTeamPlayersAmount(teamId, newPlayerAmount);
    }

    public void updateTeamPlayersAmount(String teamId, int newPlayerAmount) {
        DatabaseReference refTeamInfo = FirebaseDatabase.getInstance().getReference().child(Constants.FireBaseConstant.NODE_NAME_USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.FireBaseConstant.NODE_NAME_TEAM_INFO).child(teamId).child(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT);
        refTeamInfo.setValue(newPlayerAmount);
    }
}
