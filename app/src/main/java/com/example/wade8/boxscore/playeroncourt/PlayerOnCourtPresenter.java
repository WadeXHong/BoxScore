package com.example.wade8.boxscore.playeroncourt;

/**
 * Created by wade8 on 2018/5/3.
 */

public class PlayerOnCourtPresenter implements PlayerOnCourtContract.Presenter{

    private static final String TAG = PlayerOnCourtPresenter.class.getSimpleName();

    private final PlayerOnCourtContract.View mPlayerOnCourtView;

    public PlayerOnCourtPresenter(PlayerOnCourtContract.View playerOnCourtView) {
        mPlayerOnCourtView = playerOnCourtView;
        mPlayerOnCourtView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
