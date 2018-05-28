package com.wadexhong.boxscore.historymain;

import android.database.Cursor;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryMainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        Cursor getGameHistory();

        void transToDetail(String gameId);

        void setGameHistoryToolBar();
    }
}