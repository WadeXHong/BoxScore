package com.example.wade8.boxscore.historymain;

import android.database.Cursor;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.gamehistory.GameHistoryContract;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryMainPresenter implements HistoryMainContract.Presenter{

    private static final String TAG = HistoryMainPresenter.class.getSimpleName();

    private final HistoryMainContract.View mHistoryMainView;
    private GameHistoryContract.Presenter mGameHistoryPresenter;

    public HistoryMainPresenter(HistoryMainContract.View historyMainView, GameHistoryContract.Presenter gameHistoryPresenter) {
        mHistoryMainView = historyMainView;
        mGameHistoryPresenter = gameHistoryPresenter;

        mHistoryMainView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public Cursor getGameHistory() {
        return BoxScore.getGameInfoDbHelper().getGameHistory();
    }

    @Override
    public void transToDetail(String gameId) {
        mGameHistoryPresenter.transToDetail(gameId);
    }

    @Override
    public void setGameHistoryToolBar() {
        mGameHistoryPresenter.setGameHistoryToolBar();
    }
}
