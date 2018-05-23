package com.example.wade8.boxscore.historyteamdata;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryTeamDataPresenter implements HistoryTeamDataContract.Presenter{

    private static final String TAG = HistoryTeamDataPresenter.class.getSimpleName();

    private final HistoryTeamDataContract.View mHistoryTeamDataView;
    private HistoryTeamDataAdapter mAdapter;

    public HistoryTeamDataPresenter(HistoryTeamDataContract.View historyTeamDataView) {
        mHistoryTeamDataView = historyTeamDataView;

        mHistoryTeamDataView.setPresenter(this);
        mAdapter = new HistoryTeamDataAdapter(this);
    }

    public void setGameIdToAdapter(String gameId){
        mAdapter.refreshCursor(gameId);
    }

    @Override
    public void start() {

    }

    @Override
    public Cursor getHistoryStatistic(String gameId) {
        return BoxScore.getGameDataDbHelper().getHistoryStatisic(gameId, Constants.GameDataDBContract.COLUMN_NAME_QUARTER);
    }

    @Override
    public void setAdapter() {
        mHistoryTeamDataView.setAdapter(mAdapter);
    }

    @Override
    public Cursor getHistoryInfo(String gameId) {
        return BoxScore.getGameInfoDbHelper().getHistoryInfo(gameId);
    }

}
