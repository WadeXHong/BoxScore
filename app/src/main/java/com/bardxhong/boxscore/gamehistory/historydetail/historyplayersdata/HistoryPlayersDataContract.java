package com.bardxhong.boxscore.gamehistory.historydetail.historyplayersdata;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

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
