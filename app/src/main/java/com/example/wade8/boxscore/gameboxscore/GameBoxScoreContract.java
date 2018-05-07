package com.example.wade8.boxscore.gameboxscore;

import android.util.SparseArray;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface GameBoxScoreContract {
    interface View extends BaseView<Presenter>{

        void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter);

        void setInitDataOnScreen(SparseArray<Integer> mTeamData);

        void updateUiTeamData();

        GameInfo getGameInfo();
    }

    interface Presenter extends BasePresenter{

        void writeInitDataIntoDataBase();

        void pressYourTeamFoul(String foul);

        void pressOpponentTeamFoul(String foul);

        void pressQuarter(String quarter);

        void pressOpponentTeamScore(String score);

    }
}
