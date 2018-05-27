package com.example.wade8.boxscore.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onResume() {
        super.onResume();
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = BoxScore.sBrightness;
            getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @Override
    public void showMainUi() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hidePrograss() {

    }
}
