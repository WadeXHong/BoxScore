package com.bardxhong.boxscore.setting;

import com.bardxhong.boxscore.modelhelper.SharedPreferenceHelper;
import com.bardxhong.boxscore.BoxScore;

/**
 * Created by wade8 on 2018/5/25.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private static final String TAG = SettingPresenter.class.getSimpleName();

    private final SettingContract.View mSettingView;

    public SettingPresenter(SettingContract.View settingView) {
        mSettingView = settingView;

        mSettingView.setPresenter(this);
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
    public void signOut() {
        BoxScore.getGameDataDbHelper().deleteAll(null);
        BoxScore.getGameInfoDbHelper().deleteAll(null);
        BoxScore.getTeamDbHelper().deleteAll();
        mSettingView.finishActivity();
    }

    @Override
    public void start() {

    }
}
