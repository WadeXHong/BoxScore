package com.bardxhong.boxscore.gamehistory.historydetail.historyteamdata;

import android.database.Cursor;

import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryTeamDataContract {

    interface View extends BaseView<Presenter> {

        void setAdapter(HistoryTeamDataAdapter adapter);

        void scrollToTop();
    }

    interface Presenter extends BasePresenter {

        Cursor getHistoryStatistic(String gameId);

        Cursor getHistoryInfo(String gameId);

        void setAdapter();
    }

}
