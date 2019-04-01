package com.bardxhong.boxscore.teammanage.teamplayers.createplayer;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/21.
 */

public interface CreatePlayerContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{

        void refreshToolBar();

        void createPlayer(String teamId, Player player);

        boolean checkNumberIsExist(String teamId, String playerNumber);
    }

}
