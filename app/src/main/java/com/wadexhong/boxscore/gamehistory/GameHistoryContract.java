package com.wadexhong.boxscore.gamehistory;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

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
