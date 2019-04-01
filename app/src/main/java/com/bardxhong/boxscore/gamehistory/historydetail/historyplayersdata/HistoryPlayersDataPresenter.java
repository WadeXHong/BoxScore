package com.bardxhong.boxscore.gamehistory.historydetail.historyplayersdata;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryPlayersDataPresenter implements HistoryPlayersDataContract.Presenter {

    private static final String TAG = HistoryPlayersDataPresenter.class.getSimpleName();

    private final HistoryPlayersDataContract.View mHistoryPlayersView;

    private String mGameId;
    private HistoryPlayersDataStatisticAdapter mHistoryPlayersDataStatisticAdapter;

    public HistoryPlayersDataPresenter(HistoryPlayersDataContract.View historyPlayersView) {
        mHistoryPlayersView = historyPlayersView;
        mHistoryPlayersView.setPresenter(this);
        mHistoryPlayersDataStatisticAdapter = new HistoryPlayersDataStatisticAdapter(this);
    }

    @Override
    public void setGameIdToAdapter(String gameId) {
        mHistoryPlayersDataStatisticAdapter.refreshCursor(gameId);
    }

    @Override
    public void setAdapter() {
        mHistoryPlayersView.setAdapter(mHistoryPlayersDataStatisticAdapter);
    }

    @Override
    public void refreshUi(String gameId) {

    }

    @Override
    public void start() {

    }
}
