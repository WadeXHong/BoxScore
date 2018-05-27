package com.wadexhong.boxscore.historyplayersdata;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.adapter.HistoryPlayersDataStatisticAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryPlayersDataContract {

    interface View extends BaseView<Presenter> {

        void setAdapter(HistoryPlayersDataStatisticAdapter adapter);
    }

    interface Presenter extends BasePresenter {

        void refreshUi(String gameId);

        void setGameIdToAdapter(String gameId);

        void setAdapter();
    }

}
