package com.wadexhong.boxscore.startgame.playerlist;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface PlayerListContract {
    
    interface View extends BaseView<Presenter>{

        void getSettingResult(GameInfo gameInfo);

        int[] getCheckedInput();

        void setDefaultPlayerList(ArrayList<Player> players);
    }
    
    interface Presenter extends BasePresenter{
        
    }
    
}
