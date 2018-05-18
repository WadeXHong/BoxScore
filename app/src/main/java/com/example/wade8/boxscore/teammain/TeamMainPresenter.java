package com.example.wade8.boxscore.teammain;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamMainPresenter implements TeamMainContract.Presenter{
    private static final String TAG = TeamMainPresenter.class.getSimpleName();

    private final TeamMainContract.View mTeamMainView;

    public TeamMainPresenter(TeamMainContract.View mTeamMainView) {
        this.mTeamMainView = mTeamMainView;
    }


    @Override
    public void start() {
        mTeamMainView.setPresenter(this);
    }
}
