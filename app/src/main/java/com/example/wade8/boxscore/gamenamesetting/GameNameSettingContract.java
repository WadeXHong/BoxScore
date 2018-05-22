package com.example.wade8.boxscore.gamenamesetting;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.TeamInfo;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface GameNameSettingContract {

    interface View extends BaseView<Presenter>{

        void getSettingResult(GameInfo gameInfo);

        String[] getCheckedInput();
    }

    interface Presenter extends BasePresenter{

        ArrayList<TeamInfo> getTeamInfos();

        void setDefaultPlayerList(String teamId);
    }

}
