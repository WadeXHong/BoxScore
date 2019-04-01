package com.bardxhong.boxscore.teammanage.teammain;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamMainContract {
    interface View extends BaseView<Presenter> {

        void refreshUI();

    }
    interface Presenter extends BasePresenter {

        void queryTeamDataFromDatabase(); // call dbhelper

        void pressedCreateTeam();

        void refreshToolBar();

        void refreshUi();

        void deleteTeam(String teamId);

        void pressedTeamPlayers(String teamId);
    }
}
