package com.example.wade8.boxscore.gamenamesetting;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface GameNameSettingContract {

    interface View extends BaseView<Presenter>{

        void getSettingResult(GameInfo gameInfo);
    }

    interface Presenter extends BasePresenter{

    }

}
