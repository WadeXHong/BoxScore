package com.wadexhong.boxscore.gamenamesetting;

import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.TeamInfo;
import com.wadexhong.boxscore.BasePresenter;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface GameNameSettingContract {

    interface View extends BaseView<Presenter> {

        void getSettingResult(GameInfo gameInfo);

        String[] getCheckedInput();
    }

    interface Presenter extends BasePresenter{

        ArrayList<TeamInfo> getTeamInfos();

        void setDefaultPlayerList(String teamId);
    }

}
