package com.example.wade8.boxscore.historydetail;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryDetailContract {

    interface View extends BaseView<Presenter> {

        void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter);
    }

    interface Presenter extends BasePresenter {

    }
}
