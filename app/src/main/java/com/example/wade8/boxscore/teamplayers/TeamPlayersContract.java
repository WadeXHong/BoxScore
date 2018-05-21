package com.example.wade8.boxscore.teamplayers;

import android.database.Cursor;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

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
