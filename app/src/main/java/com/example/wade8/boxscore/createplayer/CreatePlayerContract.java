package com.example.wade8.boxscore.createplayer;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.Player;

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
