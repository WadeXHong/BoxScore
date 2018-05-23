package com.example.wade8.boxscore.historyteamdata;

import android.database.Cursor;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryTeamDataContract {

    interface View extends BaseView<Presenter> {


        void setAdapter(HistoryTeamDataAdapter adapter);
    }

    interface Presenter extends BasePresenter {

        Cursor getHistoryStatistic(String gameId);

        void setAdapter();

        Cursor getHistoryInfo(String gameId);
    }

}
