package com.bardxhong.boxscore.startgame.playerlist;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.Player;

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
