package com.wadexhong.boxscore.teammanage;

import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.BasePresenter;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamManageContract {
    interface View extends BaseView<Presenter> {

        void setTeamPlayersToolbar();

        void setTeamMainToolbar();

        void setCreatePlayerToolbar();

        void onBackPressed();
    }

    interface Presenter extends BasePresenter {

        void transToCreateTeam();

        void transToTeamPlayers(String teamId);

        void transToTeamMain();

        void setTeamMainToolbar();

        void setTeamPlayersToolbar();

        void refreshMainUi();

        void transToCreatePlayer(String teamId);

        void onBackPressed();
    }
}
