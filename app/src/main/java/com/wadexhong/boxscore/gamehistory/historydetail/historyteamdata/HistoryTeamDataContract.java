package com.wadexhong.boxscore.gamehistory.historydetail.historyteamdata;

import android.database.Cursor;

import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.adapter.HistoryTeamDataAdapter;

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

        void setAdapter();

        Cursor getHistoryInfo(String gameId);
    }

}
