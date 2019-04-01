package com.bardxhong.boxscore.gamehistory.historydetail.historyteamdata;

import android.database.Cursor;

import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryTeamDataPresenter implements HistoryTeamDataContract.Presenter {

    private static final String TAG = HistoryTeamDataPresenter.class.getSimpleName();

    private final HistoryTeamDataContract.View mHistoryTeamDataView;
    private HistoryTeamDataAdapter mHistoryTeamDataAdapter;

    public HistoryTeamDataPresenter(HistoryTeamDataContract.View historyTeamDataView) {
        mHistoryTeamDataView = historyTeamDataView;
        mHistoryTeamDataView.setPresenter(this);
        mHistoryTeamDataAdapter = new HistoryTeamDataAdapter(this);
    }

    public void setGameIdToAdapter(String gameId) {
        mHistoryTeamDataAdapter.refreshCursor(gameId);
        mHistoryTeamDataView.scrollToTop();
    }

    @Override
    public Cursor getHistoryStatistic(String gameId) {
        return BoxScore.getGameDataDbHelper()
                  .getHistoryStatistic(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                            new String[]{gameId}, Constants.GameDataDBContract.COLUMN_NAME_QUARTER,
                            Constants.GameDataDBContract.COLUMN_NAME_QUARTER);
    }

    @Override
    public void setAdapter() {
        mHistoryTeamDataView.setAdapter(mHistoryTeamDataAdapter);
    }

    @Override
    public Cursor getHistoryInfo(String gameId) {
        return BoxScore.getGameInfoDbHelper().getSpecificInfo(gameId);
    }

    @Override
    public void start() {

    }

}
