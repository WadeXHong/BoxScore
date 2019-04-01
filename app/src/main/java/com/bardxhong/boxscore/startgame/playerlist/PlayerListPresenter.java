package com.bardxhong.boxscore.startgame.playerlist;

import android.database.Cursor;

import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public class PlayerListPresenter implements PlayerListContract.Presenter {

    private final String TAG = PlayerListPresenter.class.getSimpleName();

    private final PlayerListContract.View mPlayerListView;

    public PlayerListPresenter(PlayerListContract.View playerListView) {
        mPlayerListView = playerListView;
    }

    public void getDataFromView(GameInfo gameInfo) {
        mPlayerListView.getSettingResult(gameInfo);
    }

    public boolean checkInputIsLegal() {
        int[] input = mPlayerListView.getCheckedInput();
        return input[0] == 5 && input[1] > 0;
    }

    public void setDefaultPlayerList(String teamId) {

        ArrayList<Player> players = new ArrayList<>();

        Cursor cursor = BoxScore.getTeamDbHelper().getPlayersFromDb(teamId);
        int size = cursor.getCount();
        for (int i = 0; i < size; i++) {
            cursor.moveToPosition(i);
            players.add(new Player(cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER)),
                      cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME)),
                      cursor.getString(cursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID))));
        }

        mPlayerListView.setDefaultPlayerList(players);
    }

    @Override
    public void start() {

    }
}
