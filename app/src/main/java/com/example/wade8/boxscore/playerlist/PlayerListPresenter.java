package com.example.wade8.boxscore.playerlist;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public class PlayerListPresenter implements PlayerListContract.Presenter{

    private static final String TAG = PlayerListPresenter.class.getSimpleName();

    private final PlayerListContract.View mPlayerListView;

    public PlayerListPresenter(PlayerListContract.View playerListView) {
        this.mPlayerListView = playerListView;
    }

    @Override
    public void start() {

    }

    public void getDataFromView(GameInfo gameInfo) {
        mPlayerListView.getSettingResult(gameInfo);
    }

    public boolean checkInputIsLegal() {
        int[] input = mPlayerListView.getCheckedInput();
        return input[0] == 5 && input[1]>0;
    }

    public void setDefaultPlayerList(String teamId) {

        Cursor cursor = BoxScore.getTeamDbHelper().getPlayersFromDb(teamId);
        int size = cursor.getCount();

        ArrayList<Player> players = new ArrayList<>();
        for (int i=0 ; i<size ; i++){
            cursor.moveToPosition(i);
            players.add(new Player(cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.PLAYER_NUMBER)),
                      cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.PLAYER_NAME)),
                      cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.PLAYER_ID))));
        }

        mPlayerListView.setDefaultPlayerList(players);
    }
}
