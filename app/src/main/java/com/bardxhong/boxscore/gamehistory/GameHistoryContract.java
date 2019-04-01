package com.bardxhong.boxscore.gamehistory;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface GameHistoryContract {

    interface View extends BaseView<Presenter> {

        void setGameHistoryToolBar();

        void setHistoryDetailToolBar();
    }

    interface Presenter extends BasePresenter {

        void transToDetail(String gameId);

        void transToMain();

        void setGameHistoryToolBar();

        void setHistoryDetailToolBar();


    }

}
