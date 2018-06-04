package com.wadexhong.boxscore.teammanage.teammain;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

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
