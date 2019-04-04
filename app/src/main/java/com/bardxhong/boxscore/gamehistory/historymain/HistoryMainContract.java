package com.bardxhong.boxscore.gamehistory.historymain;

import android.database.Cursor;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryMainContract {

    interface View extends BaseView<Presenter> {

        void confirmShareGameHistory(String gameId);

        void saveUrlInClipboard(String string);

        void showToast(String message);
    }

    interface Presenter extends BasePresenter {

        Cursor getGameHistory();

        void transToDetail(String gameId);

        void setGameHistoryToolBar();

        void confirmShareGameHistory(String gameId);

        void deleteGameHistory(String teamId, String gameId);

        void createAndShareGameHistoryXls(String gameId);
    }
}