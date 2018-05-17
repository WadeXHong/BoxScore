package com.example.wade8.boxscore.startgame;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface StartGameContract {

    interface View extends BaseView<Presenter>{

        void showMainUi();

        void showTeamInSpinner();

        void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter);

        void setGameNameSettingToolBar();

        void setPlayerListToolBar();

        void setDetailSettingToolBar();

        void setViewPagerCurrentItem(boolean isChangePageAllowed);
    }

    interface Presenter extends BasePresenter{

        void getTeamFromFireBase();

        void onPageSelected(int position);

        void transToGameNameSettingPage();

        void transToPlayerSettingPage();

        void transToDetailSettingPage();

        GameInfo getSettingResult(GameInfo gameInfo);

        void checkInput(int currentItem);
    }
}
