package com.wadexhong.boxscore.teamplayers;

import android.database.Cursor;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamPlayersContract {

    interface View extends BaseView<Presenter>{

        void refreshUi();
    }

    interface Presenter extends BasePresenter{

        Cursor getPlayers(String teamId);

        void refreshUi();

        void pressedCreatePlayer(String teamId);
    }
}
