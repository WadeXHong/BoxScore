package com.bardxhong.boxscore.startgame;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.bardxhong.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public interface StartGameContract {

    interface View extends BaseView<Presenter>{

        void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter);

        void setGameNameSettingToolBar();

        void setPlayerListToolBar();

        void setDetailSettingToolBar();

        void setViewPagerCurrentItem(boolean isChangePageAllowed);

        void showMainUi();

        void noLegalTeam();
    }

    interface Presenter extends BasePresenter{

        void onPageSelected(int position);

        GameInfo getSettingResult(GameInfo gameInfo);

        void checkInput(int currentItem);

        void setDefaultPlayerList(String teamId);

        void transToGameNameSettingPage();

        void transToPlayerSettingPage();

        void transToDetailSettingPage();

        void noLegalTeam();
    }
}
