package com.example.wade8.boxscore.historyplayersdata;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryPlayersPresenter implements HistoryPlayersContract.Presenter{

    private static final String TAG = HistoryPlayersPresenter.class.getSimpleName();

    private final HistoryPlayersContract.View mHistoryPlayersView;

    public HistoryPlayersPresenter(HistoryPlayersContract.View historyPlayersView) {
        mHistoryPlayersView = historyPlayersView;

        mHistoryPlayersView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
