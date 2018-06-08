package com.wadexhong.boxscore.gamehistory.historydetail.historyplayersdata;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryPlayersDataContract {

    interface View extends BaseView<Presenter> {

        void setAdapter(HistoryPlayersDataStatisticAdapter adapter);
    }

    interface Presenter extends BasePresenter {

        void setGameIdToAdapter(String gameId);

        void setAdapter();

        void refreshUi(String gameId);
    }

}
