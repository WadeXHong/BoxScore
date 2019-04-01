package com.bardxhong.boxscore.startgame.gamenamesetting;

import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.TeamInfo;
import com.bardxhong.boxscore.BasePresenter;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface GameNameSettingContract {

    interface View extends BaseView<Presenter> {

        String[] getCheckedInput();

        void getSettingResult(GameInfo gameInfo);
    }

    interface Presenter extends BasePresenter{

        ArrayList<TeamInfo> getTeamInfos();

        void setDefaultPlayerList(String teamId);
    }

}
