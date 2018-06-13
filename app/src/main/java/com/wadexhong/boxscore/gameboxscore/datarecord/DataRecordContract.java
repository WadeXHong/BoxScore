package com.wadexhong.boxscore.gameboxscore.datarecord;

import com.wadexhong.boxscore.gameboxscore.datarecord.playerselect.PlayerSelectDialog;
import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface DataRecordContract {

    interface View extends BaseView<Presenter> {

        void popPlayerSelectDialog(PlayerSelectDialog dialog, int type);

        void popIsShotMadeDialog(int type);

        void enableAllButtons(boolean setEnableOrNot);

        void popFoulTypeDialog(int type);
    }

    interface Presenter extends BasePresenter {

        GameInfo getGameInfo();

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

        void pressOffensiveFoul(int type);

        void pressDefensiveFoul(int type);

        void updateUiGameBoxActivity();

        void callActivityPresenterEditDataInDb(Player player, int type);

    }

}
