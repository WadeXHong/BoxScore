package com.example.wade8.boxscore.createteam;

import android.util.Log;

import com.example.wade8.boxscore.BoxScore;

/**
 * Created by wade8 on 2018/5/18.
 */

public class CreateTeamPresenter implements CreateTeamContract.Presenter{

    private static final String TAG = CreateTeamPresenter.class.getSimpleName();

    private final CreateTeamContract.View mCreateTeamView;

    public CreateTeamPresenter(CreateTeamContract.View createTeamView) {
        mCreateTeamView = createTeamView;

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
            }
        }
    }
}
