package com.example.wade8.boxscore.setting;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.SharedPreferenceHelper;

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

    @Override
    public void setBrightness(float brightness) {
        mSettingView.setBrightness(brightness);
        SharedPreferenceHelper.write(SharedPreferenceHelper.BRIGHTNESS, brightness);
        BoxScore.sBrightness = brightness;
    }

    @Override
    public void unManualBrightness() {
        SharedPreferenceHelper.remove(SharedPreferenceHelper.BRIGHTNESS);
        setBrightness(-1f);
    }
}
