package com.wadexhong.boxscore.gameboxscore.datarecord.playerselect;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface PlayerSelectContract {
    interface View extends BaseView<Presenter>{

        void dismiss();
    }

    interface Presenter extends BasePresenter{

        void editDataInDB(Player player, int type);

        ArrayList<Player> getPlayerOnCourt();
    }
}
