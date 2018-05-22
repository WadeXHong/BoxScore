package com.example.wade8.boxscore.gamehistory;

/**
 * Created by wade8 on 2018/5/22.
 */

public class GameHistoryPresenter implements GameHistoryContract.Presenter{

    private static final String TAG = GameHistoryPresenter.class.getSimpleName();

    private final GameHistoryContract.View mGameHistoryView;

    public GameHistoryPresenter(GameHistoryContract.View gameHistoryView) {
        mGameHistoryView = gameHistoryView;

        mGameHistoryView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
