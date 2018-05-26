package com.example.wade8.boxscore.setting;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/25.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter>{

        void setBrightness(float brightness);
    }

    interface Presenter extends BasePresenter{

        void setBrightness(float brightness);

        void unManualBrightness();
    }

}
