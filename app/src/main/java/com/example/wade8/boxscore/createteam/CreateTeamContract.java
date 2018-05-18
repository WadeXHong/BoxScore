package com.example.wade8.boxscore.createteam;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/18.
 */

public interface CreateTeamContract {
    
    interface View extends BaseView<Presenter>{

        void showIllegalHint();
    }
    
    interface Presenter extends BasePresenter{

        void pressedConfirm(String teamName);
    }
    
}
