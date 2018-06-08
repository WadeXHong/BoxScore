package com.wadexhong.boxscore.gamehistory.historymain;

import android.database.Cursor;

import com.wadexhong.boxscore.gamehistory.GameHistoryContract;
import com.wadexhong.boxscore.BoxScore;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryMainPresenter implements HistoryMainContract.Presenter {

    private static final String TAG = HistoryMainPresenter.class.getSimpleName();

    private final HistoryMainContract.View mHistoryMainView;
    private GameHistoryContract.Presenter mGameHistoryPresenter;

    public HistoryMainPresenter(HistoryMainContract.View historyMainView, GameHistoryContract.Presenter gameHistoryPresenter) {
        mHistoryMainView = historyMainView;
        mGameHistoryPresenter = gameHistoryPresenter;

        mHistoryMainView.setPresenter(this);
    }

    @Override
    public Cursor getGameHistory() {
        return BoxScore.getGameInfoDbHelper().getGameHistory();
    }

    @Override
    public void setGameHistoryToolBar() {
        mGameHistoryPresenter.setGameHistoryToolBar();
    }

    @Override
    public void transToDetail(String gameId) {
        mGameHistoryPresenter.transToDetail(gameId);
    }

    @Override
    public void start() {

    }
}
