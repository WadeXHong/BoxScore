package com.example.wade8.boxscore.teamplayers;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamPlayersPresenter implements TeamPlayersContract.Presenter{

    private static final String TAG = TeamPlayersPresenter.class.getSimpleName();

    private final TeamPlayersContract.View mCreatePlayerView;

    public TeamPlayersPresenter(TeamPlayersContract.View createPlayerView) {
        mCreatePlayerView = createPlayerView;

        mCreatePlayerView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public Cursor getPlayers(String teamId) {
        return BoxScore.getTeamDbHelper().getPlayersFromDb(teamId);
    }

    @Override
    public void refreshUi() {
        mCreatePlayerView.refreshUi();
    }
}
