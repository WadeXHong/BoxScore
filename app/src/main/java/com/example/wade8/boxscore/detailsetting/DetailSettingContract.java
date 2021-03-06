package com.example.wade8.boxscore.detailsetting;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface DetailSettingContract {

    interface View extends BaseView<Presenter>{

        void updateMaxFoulInEditText(String s);
        void updateQuarterLengthEditText(String s);
        void updateTotalQuarterEditText(String s);
        void updateTimeoutsFirstHalfEditText(String s);
        void updateTimeoutsSecondHalfEditText(String s);
        void showToast(String message);

        void getSettingResult(GameInfo gameInfo);
    }

    interface Presenter extends BasePresenter{

        void maxFoulPlus(String input);
        void maxFoulMinus(String input);

        void quarterLengthPlus(String input);
        void quarterLengthMinus(String input);

        void totalQuarterPlus(String input);
        void totalQuarterMinus(String input);

        void timeoutsFirstHalfPlus(String input);
        void timeoutsFirstHalfMinus(String input);

        void timeoutsSecondHalfPlus(String input);
        void timeoutsSecondHalfMinus(String input);
    }

}
