package com.example.wade8.boxscore.historymain;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryMainPresenter implements HistoryMainContract.Presenter{

    private static final String TAG = HistoryMainPresenter.class.getSimpleName();

    private final HistoryMainContract.View mHistoryMainView;

    public HistoryMainPresenter(HistoryMainContract.View historyMainView) {
        mHistoryMainView = historyMainView;

        mHistoryMainView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public Cursor getGameHistory() {
        return BoxScore.getGameInfoDbHelper().getGameHistory();
    }
}
