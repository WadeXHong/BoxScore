package com.example.wade8.boxscore.playeroncourt;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.PlayerOnCourtAdapter;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface PlayerOnCourtContract {

    interface View extends BaseView<Presenter>{

        void setAdapter(PlayerOnCourtAdapter mAdapter);
    }

    interface Presenter extends BasePresenter{

    }
}
