package com.example.wade8.boxscore.historyteamdata;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryTeamDataPresenter implements HistoryTeamDataContract.Presenter{

    private static final String TAG = HistoryTeamDataPresenter.class.getSimpleName();

    private final HistoryTeamDataContract.View mHistoryTeamDataView;

    public HistoryTeamDataPresenter(HistoryTeamDataContract.View historyTeamDataView) {
        mHistoryTeamDataView = historyTeamDataView;
        
        mHistoryTeamDataView.setPresenter(this);
    }


    @Override
    public void start() {

    }
}
