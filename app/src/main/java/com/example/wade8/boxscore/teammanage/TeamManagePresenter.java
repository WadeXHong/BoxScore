package com.example.wade8.boxscore.teammanage;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamManagePresenter implements TeamManageContract.Presenter {
    private static final String TAG = TeamManagePresenter.class.getSimpleName();

    private final TeamManageContract.View mTeamManageView;

    public TeamManagePresenter(TeamManageContract.View mTeamManageView) {
        this.mTeamManageView = mTeamManageView;

    }

    @Override
    public void start() {
        mTeamManageView.setPresenter(this);
    }
}
