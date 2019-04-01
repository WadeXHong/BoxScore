package com.bardxhong.boxscore.gameboxscore.changeplayer;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.gameboxscore.changeplayer.changedialog.ChangePlayerDialog;
import com.bardxhong.boxscore.objects.GameInfo;

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

        GameInfo getGameInfo();

        void inGamePlayerSelected(int mPositionInBenchArray);

        void offGamePlayerSelected(int mPositionInOnCourtArray);

        void updateFragmentUi();
    }
}
