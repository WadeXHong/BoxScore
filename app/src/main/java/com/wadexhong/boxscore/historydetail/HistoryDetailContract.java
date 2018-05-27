package com.wadexhong.boxscore.historydetail;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.ViewPagerFragmentAdapter;

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
