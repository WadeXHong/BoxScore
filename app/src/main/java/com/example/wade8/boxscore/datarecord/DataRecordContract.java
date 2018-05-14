package com.example.wade8.boxscore.datarecord;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface DataRecordContract {
    interface View extends BaseView<Presenter>{

        void popPlayerSelectDialog(PlayerSelectDialog dialog, int type);
        void popIsShotMadeDialog(int type);
        void enableAllButtons(boolean setEnableOrNot);

    }

    interface Presenter extends BasePresenter{
        void pressTwoPoint();
        void pressTwoPointMade();
        void pressTwoPointMissed();
        void pressThreePoint();
        void pressThreePointMade();
        void pressThreePointMissed();
        void pressFreeThrow();
        void pressFreeThrowMade();
        void pressFreeThrowMissed();
        void pressAssist();
        void pressOffensiveRebound();
        void pressSteal();
        void pressBlock();
        void pressFoul();
        void pressTurnover();
        void pressDefensiveRebound();
        void pressShotMade(int type);
        void pressShotMissed(int type);
        GameInfo getGameInfo();
        void updateUiGameBoxActivity();

        void callActivityPresenterEditDataInDb(int position, int type);
    }

}
