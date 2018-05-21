package com.example.wade8.boxscore.createplayer;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/21.
 */

public interface CreatePlayerContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{

        void refreshToolBar();
    }

}
