package com.bardxhong.boxscore.teammanage.teamplayers;

import android.database.Cursor;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface TeamPlayersContract {

    interface View extends BaseView<Presenter> {

        void refreshUi();
    }

    interface Presenter extends BasePresenter {

        Cursor getPlayers(String teamId);

        void pressedCreatePlayer(String teamId);

        void deletePlayer(String teamId, String playerId);

        void refreshUi();

        void transToPlayerDetail(String playerId);
    }
}
