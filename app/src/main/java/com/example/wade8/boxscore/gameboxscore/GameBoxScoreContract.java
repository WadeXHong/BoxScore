package com.example.wade8.boxscore.gameboxscore;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface GameBoxScoreContract {
    interface View extends BaseView<Presenter>{

        void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter);
    }

    interface Presenter extends BasePresenter{

    }
}
