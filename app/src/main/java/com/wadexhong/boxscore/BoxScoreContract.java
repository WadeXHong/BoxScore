package com.wadexhong.boxscore;


/**
 * Created by wade8 on 2018/5/1.
 */

public interface BoxScoreContract {

    interface View extends BaseView<Presenter>{

        void showMainUi(int logInViewVisibility, int fuctionViewVisibility);


        void askResumeGame(String opponentName);

        void transToStartGame();

        void transToGameBoxScore();

        void showProgressBarDialog(String message);
    }

    interface Presenter extends BasePresenter{

        void transToStartGame();

        void transToTeamManage();

        void transToGameHistory();

        void transToSetting();

        void pressStartGame();

        void removeGameDataSharedPreferences();

        void removeGameDataInDataBase();

        void signUpFireBase(String userName, String password);

        void logInFireBase(String userEmail, String password);

        void updateDbFromFireBase();

        void saveAndEndCurrentGame();
    }

}
