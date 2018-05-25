package com.example.wade8.boxscore.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wade8.boxscore.R;

public class SettingActivity extends AppCompatActivity implements SettingContract.View{

    private static final String TAG = SettingActivity.class.getSimpleName();

    private SettingContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPresenter = new SettingPresenter(this);
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
