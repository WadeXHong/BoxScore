package com.bardxhong.boxscore.teammanage.createteam;

import android.util.Log;

import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.teammanage.TeamManageContract;

/**
 * Created by wade8 on 2018/5/18.
 */

public class CreateTeamPresenter implements CreateTeamContract.Presenter{

    private static final String TAG = CreateTeamPresenter.class.getSimpleName();

    private final CreateTeamContract.View mCreateTeamView;
    private TeamManageContract.Presenter mTeamManagePresenter;

    public CreateTeamPresenter(CreateTeamContract.View createTeamView, TeamManageContract.Presenter teamManagePresenter) {
        mCreateTeamView = createTeamView;
        mTeamManagePresenter = teamManagePresenter;
        mCreateTeamView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void pressedConfirm(String teamName) {
        Log.d(TAG,"pressedConfirm executed");
        if (teamName.trim().equals("")){
            mCreateTeamView.showIllegalHint();
        }else {
            if (!BoxScore.getTeamDbHelper().createTeamInDB(teamName))
                mCreateTeamView.showIllegalHint();
            else {
                mCreateTeamView.dismissAllowingStateLoss();
                mCreateTeamView.resetUi();
                mTeamManagePresenter.refreshMainUi();
            }
        }
    }
}