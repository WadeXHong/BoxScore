package com.bardxhong.boxscore.teammanage.createteam;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface CreateTeamContract {
    
    interface View extends BaseView<Presenter>{

        void showIllegalHint();

        void dismissAllowingStateLoss();

        void resetUi();
    }
    
    interface Presenter extends BasePresenter{

        void pressedConfirm(String teamName);
    }
    
}
