package com.wadexhong.boxscore.login;

/**
 * Created by wade8 on 2018/5/1.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View loginView) {
        this.mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login() {

    }

    @Override
    public void signUp() {

    }
}
