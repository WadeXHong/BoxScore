package com.example.wade8.boxscore.historyplayersdata;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.HistoryPlayersDataStatisticAdapter;

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
