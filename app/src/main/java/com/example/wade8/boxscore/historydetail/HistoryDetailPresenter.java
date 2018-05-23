package com.example.wade8.boxscore.historydetail;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryDetailPresenter implements HistoryDetailContract.Presenter {

    private static final String TAG = HistoryDetailPresenter.class.getSimpleName();

    private final HistoryDetailContract.View mHistoryDetailView;

    public HistoryDetailPresenter(HistoryDetailContract.View historyDetailView) {
        mHistoryDetailView = historyDetailView;

        mHistoryDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void refreshUi(String gameId) {
        mHistoryDetailView.refreshUi();
    }
}
