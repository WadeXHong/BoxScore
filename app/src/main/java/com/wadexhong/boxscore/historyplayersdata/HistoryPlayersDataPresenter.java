package com.wadexhong.boxscore.historyplayersdata;

import com.wadexhong.boxscore.adapter.HistoryPlayersDataStatisticAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryPlayersDataPresenter implements HistoryPlayersDataContract.Presenter{

    private static final String TAG = HistoryPlayersDataPresenter.class.getSimpleName();

    private final HistoryPlayersDataContract.View mHistoryPlayersView;
    private String mGameId;
    private HistoryPlayersDataStatisticAdapter mAdapter;

    public HistoryPlayersDataPresenter(HistoryPlayersDataContract.View historyPlayersView) {
        mHistoryPlayersView = historyPlayersView;
        mHistoryPlayersView.setPresenter(this);
        mAdapter = new HistoryPlayersDataStatisticAdapter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void refreshUi(String gameId) {

    }

    @Override
    public void setGameIdToAdapter(String gameId) {
        mAdapter.refreshCursor(gameId);
    }

    @Override
    public void setAdapter() {
        mHistoryPlayersView.setAdapter(mAdapter);
    }
}
