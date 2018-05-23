package com.example.wade8.boxscore.historyteamdata;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;

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

    @Override
    public Cursor getHistoryStatistic(String gameId) {
        return BoxScore.getGameDataDbHelper().getHistoryStatisic(gameId, Constants.GameDataDBContract.COLUMN_NAME_QUARTER);
    }

    @Override
    public void refreshUi(String gameId) {
        mHistoryTeamDataView.refreshUi(gameId);
    }
}
