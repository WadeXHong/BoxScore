package com.example.wade8.boxscore.startgame;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface StartGameContract {

    interface View extends BaseView<Presenter>{

        void showMainUi();

        void showTeamInSpinner();

        void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter);
    }

    interface Presenter extends BasePresenter{

        void getTeamFromFireBase();
    }
}
