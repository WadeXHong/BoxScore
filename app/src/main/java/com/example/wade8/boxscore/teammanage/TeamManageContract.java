package com.example.wade8.boxscore.teammanage;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamManageContract {
    interface View extends BaseView<Presenter>{

        void setTeamPlayersToolbar();

        void setTeamMainToolbar();

    }

    interface Presenter extends BasePresenter{

        void transToCreateTeam();

        void transToTeamPlayers();

        void transToTeamMain();

        void setTeamMainToolbar();

        void setTeamPlayersToolbar();

        void refreshMainUi();
    }
}
