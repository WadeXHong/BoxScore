package com.example.wade8.boxscore;


/**
 * Created by wade8 on 2018/5/1.
 */

public interface BoxScoreContract {

    interface View extends BaseView<Presenter>{

        void showMainUi();


        void askResumeGame(String opponentName);

        void transToStartGame();

        void transToGameBoxScore();
    }

    interface Presenter extends BasePresenter{

        void transToStartGame();

        void transToTeamManage();

        void transToGameHistory();

        void transToSetting();

        void pressStartGame();

        void clearPreviousGameData();
    }

}
