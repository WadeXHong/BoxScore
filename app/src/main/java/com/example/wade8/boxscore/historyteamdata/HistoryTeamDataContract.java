package com.example.wade8.boxscore.historyteamdata;

import android.database.Cursor;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryTeamDataContract {

    interface View extends BaseView<Presenter> {

        void refreshUi(String gameId);
    }

    interface Presenter extends BasePresenter {

        Cursor getHistoryStatistic(String gameId);

        void refreshUi(String gameId);
    }

}
