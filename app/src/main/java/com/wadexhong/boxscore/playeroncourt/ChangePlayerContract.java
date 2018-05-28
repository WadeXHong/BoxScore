package com.wadexhong.boxscore.playeroncourt;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.adapter.ChangePlayerAdapter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.playeroncourt.changedialog.ChangePlayerDialog;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface ChangePlayerContract {

    interface View extends BaseView<Presenter>{

        void setAdapter(ChangePlayerAdapter mAdapter);

        void popInGamePlayerDialog(ChangePlayerDialog dialog);

        void popOffGamePlayerDialog(ChangePlayerDialog dialog);

        void updateUi();
    }

    interface Presenter extends BasePresenter{

        void offGamePlayerSelected(int mPositionInOnCourtArray);

        void inGamePlayerSelected(int mPositionInBenchArray);

        GameInfo getGameInfo();

        void updateFragmentUi();
    }
}