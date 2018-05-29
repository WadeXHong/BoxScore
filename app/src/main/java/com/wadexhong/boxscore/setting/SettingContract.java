package com.wadexhong.boxscore.setting;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/25.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter>{

        void setBrightness(float brightness);

        void finishActivity();
    }

    interface Presenter extends BasePresenter{

        void setBrightness(float brightness);

        void unManualBrightness();

        void finishActivity();
    }

}
