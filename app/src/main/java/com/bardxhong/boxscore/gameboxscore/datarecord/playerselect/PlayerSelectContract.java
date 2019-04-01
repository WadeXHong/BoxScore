package com.bardxhong.boxscore.gameboxscore.datarecord.playerselect;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface PlayerSelectContract {

    interface View extends BaseView<Presenter> {

        void dismiss();
    }

    interface Presenter extends BasePresenter {

        ArrayList<Player> getPlayerOnCourt();

        void editDataInDB(Player player, int type);
    }
}
