package com.example.wade8.boxscore.login;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter>{
        void showMainUi();
        void showProgress();
        void hidePrograss();

    }

    interface Presenter extends BasePresenter{
        void login();
        void signUp();
    }
}
