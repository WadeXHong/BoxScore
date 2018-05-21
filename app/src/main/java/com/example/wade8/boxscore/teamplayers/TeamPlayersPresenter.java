package com.example.wade8.boxscore.teamplayers;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.teammanage.TeamManageContract;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamPlayersPresenter implements TeamPlayersContract.Presenter{

    private static final String TAG = TeamPlayersPresenter.class.getSimpleName();

    private final TeamPlayersContract.View mCreatePlayerView;
    private TeamManageContract.Presenter mTeamManagePresenter;

    public TeamPlayersPresenter(TeamPlayersContract.View createPlayerView, TeamManageContract.Presenter teamManagePresenter) {
        mCreatePlayerView = createPlayerView;
        mTeamManagePresenter = teamManagePresenter;

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

    @Override
    public void pressedCreatePlayer() {
        mTeamManagePresenter.transToCreatePlayer();
    }
}
