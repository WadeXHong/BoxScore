package com.wadexhong.boxscore.startgame;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.objects.GameInfo;

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
