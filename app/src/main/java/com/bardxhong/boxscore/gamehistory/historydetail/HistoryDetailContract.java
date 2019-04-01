package com.bardxhong.boxscore.gamehistory.historydetail;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.adapter.ViewPagerFragmentAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryDetailContract {

    interface View extends BaseView<Presenter> {

        void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter);

        void setViewPagerPage();
    }

    interface Presenter extends BasePresenter {

    }
}
