package com.bardxhong.boxscore.setting;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/25.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter> {

        void setBrightness(float brightness);

        void finishActivity();
    }

    interface Presenter extends BasePresenter {

        void setBrightness(float brightness);

        void unManualBrightness();

        void signOut();
    }

}
