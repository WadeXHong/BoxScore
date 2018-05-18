package com.example.wade8.boxscore.teamplayers;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamPlayersPresenter implements TeamPlayersContract.Presenter{

    private static final String TAG = TeamPlayersPresenter.class.getSimpleName();

    private final TeamPlayersContract.View mCreateTeamView;

    public TeamPlayersPresenter(TeamPlayersContract.View createTeamView) {
        mCreateTeamView = createTeamView;

        mCreateTeamView.setPresenter(this);
    }


    @Override
    public void start() {

    }
}
