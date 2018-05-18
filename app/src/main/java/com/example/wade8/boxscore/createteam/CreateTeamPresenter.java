package com.example.wade8.boxscore.createteam;

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
}
