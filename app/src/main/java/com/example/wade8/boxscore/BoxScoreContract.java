package com.example.wade8.boxscore;


/**
 * Created by wade8 on 2018/5/1.
 */

public interface BoxScoreContract {

    interface View extends BaseView<Presenter>{

        void showMainUi();


    }

    interface Presenter extends BasePresenter{

        void transToGame();

        void transToTeamManage();

        void transToGameHistory();

        void transToSetting();

    }

}
