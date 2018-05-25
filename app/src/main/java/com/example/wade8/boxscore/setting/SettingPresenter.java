package com.example.wade8.boxscore.setting;

/**
 * Created by wade8 on 2018/5/25.
 */

public class SettingPresenter implements SettingContract.Presenter{

    private static final String TAG = SettingPresenter.class.getSimpleName();

    private final SettingContract.View mSettingView;

    public SettingPresenter(SettingContract.View settingView) {
        mSettingView = settingView;

        mSettingView.setPresenter(this);
    }


    @Override
    public void start() {

    }
}
