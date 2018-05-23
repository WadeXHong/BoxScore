package com.example.wade8.boxscore.historyplayersdata;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryPlayersContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void refreshUi(String gameId);
    }

}
