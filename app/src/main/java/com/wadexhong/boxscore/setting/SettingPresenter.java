package com.wadexhong.boxscore.setting;

import com.wadexhong.boxscore.SharedPreferenceHelper;
import com.wadexhong.boxscore.BoxScore;

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

    @Override
    public void finishActivity() {
        mSettingView.finishActivity();
    }
}
