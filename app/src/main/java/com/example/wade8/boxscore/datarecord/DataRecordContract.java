package com.example.wade8.boxscore.datarecord;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;

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
        void PressTwoPoint();
        void PressThreePoint();
        void PressFreeThrow();
        void PressAssist();
        void PressOffensiveRebound();
        void PressSteal();
        void PressBlock();
        void PressFoul();
        void PressTurnover();
        void PressDefensiveRebound();

        void PressShotMade(int type);

        void PressShotMissed(int type);
    }

}
