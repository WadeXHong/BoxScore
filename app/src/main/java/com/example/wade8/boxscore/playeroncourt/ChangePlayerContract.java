package com.example.wade8.boxscore.playeroncourt;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.ChangePlayerAdapter;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.playeroncourt.changedialog.ChangePlayerDialog;

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
