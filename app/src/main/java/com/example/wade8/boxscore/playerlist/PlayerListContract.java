package com.example.wade8.boxscore.playerlist;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;

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
