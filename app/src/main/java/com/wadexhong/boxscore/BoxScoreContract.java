package com.wadexhong.boxscore;


/**
 * Created by wade8 on 2018/5/1.
 */

public interface BoxScoreContract {

    interface View extends BaseView<Presenter> {

        void askResumeGame(String opponentName);

        void transToStartGame();

        void transToGameBoxScore();

        void showProgressBarDialog(String message);

        void showMainUi(int logInViewVisibility, int functionViewVisibility);
    }

    interface Presenter extends BasePresenter {

        void transToStartGame();

        void transToTeamManage();

        void transToGameHistory();

        void transToSetting();

        void pressStartGame();

        void saveAndEndCurrentGame();

        void removeGameDataSharedPreferences();

        void removeGameDataInDataBase();

        void updateDbFromFireBase();
    }

}
