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
    }

    interface Presenter extends BasePresenter{

        void writeInitDataIntoDataBase(GameInfo gameInfo);

        void pressYourTeamFoul(String foul, GameInfo gameInfo);

        void pressOpponentTeamFoul(String foul, GameInfo gameInfo);

        void pressQuarter(String quarter, GameInfo gameInfo);

        void pressOpponentTeamScore(String score);
    }
}
