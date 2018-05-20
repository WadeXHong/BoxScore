package com.example.wade8.boxscore.teammain;

import com.example.wade8.boxscore.teammanage.TeamManageContract;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamMainPresenter implements TeamMainContract.Presenter{
    private static final String TAG = TeamMainPresenter.class.getSimpleName();

    private final TeamMainContract.View mTeamMainView;
    private TeamManageContract.Presenter mTeamManagePresenter;

    public TeamMainPresenter(TeamMainContract.View teamMainView, TeamManageContract.Presenter teamManagePresenter) {
        mTeamMainView = teamMainView;
        mTeamManagePresenter = teamManagePresenter;

        mTeamMainView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void queryTeamDataFromDatabase() {

    }

    @Override
    public void pressedCreateTeam() {
        mTeamManagePresenter.transToCreateTeam();
    }

    @Override
    public void refreshToolBar() {
        mTeamManagePresenter.setTeamMainToolbar();
    }

    @Override
    public void refreshUi() {
        mTeamMainView.refreshUI();
    }
}
