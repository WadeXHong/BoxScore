package com.example.wade8.boxscore;

/**
 * Created by wade8 on 2018/5/1.
 */

public class BoxScorePresenter implements BoxScoreContract.Presenter {

    private static final String TAG =BoxScorePresenter.class.getSimpleName();

    private final BoxScoreContract.View mBoxScoreView;

    public BoxScorePresenter(BoxScoreContract.View boxScoreView) {
        this.mBoxScoreView = boxScoreView;
        mBoxScoreView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void transToGame() {

    }

    @Override
    public void transToTeamManage() {

    }

    @Override
    public void transToGameHistory() {

    }

    @Override
    public void transToSetting() {

    }
}
