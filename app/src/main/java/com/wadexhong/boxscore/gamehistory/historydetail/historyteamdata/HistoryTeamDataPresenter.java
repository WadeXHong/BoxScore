package com.wadexhong.boxscore.gamehistory.historydetail.historyteamdata;

import android.database.Cursor;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.adapter.HistoryTeamDataAdapter;

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
        mHistoryTeamDataView.scrollToTop();
    }

    @Override
    public void start() {

    }

    @Override
    public Cursor getHistoryStatistic(String gameId) {
        return BoxScore.getGameDataDbHelper()
                  .getHistoryStatistic(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                            new String[]{gameId}, Constants.GameDataDBContract.COLUMN_NAME_QUARTER, Constants.GameDataDBContract.COLUMN_NAME_QUARTER);
    }

    @Override
    public void setAdapter() {
        mHistoryTeamDataView.setAdapter(mAdapter);
    }

    @Override
    public Cursor getHistoryInfo(String gameId) {
        return BoxScore.getGameInfoDbHelper().getSpecificInfo(gameId);
    }

}
