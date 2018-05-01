package com.example.wade8.boxscore.startgame;

/**
 * Created by wade8 on 2018/5/1.
 */

public class StartGamePresenter implements StartGameContract.Presenter{

    private final StartGameContract.View mStartGameView;

    public StartGamePresenter(StartGameContract.View startGameView) {
        this.mStartGameView = startGameView;
        mStartGameView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getTeamFromFireBase() {

    }
}
