package com.wadexhong.boxscore.teammanage.teamplayers.createplayer;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.Player;

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
