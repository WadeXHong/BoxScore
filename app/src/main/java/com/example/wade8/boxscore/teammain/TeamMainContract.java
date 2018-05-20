package com.example.wade8.boxscore.teammain;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamMainContract {
    interface View extends BaseView<Presenter>{

        void refreshUI();

    }
    interface Presenter extends BasePresenter{

        void queryTeamDataFromDatabase(); // call dbhelper

        void pressedCreateTeam();

        void refreshToolBar();

        void refreshUi();
    }
}
