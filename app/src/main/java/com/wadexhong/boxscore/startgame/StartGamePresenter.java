package com.wadexhong.boxscore.startgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.startgame.detailsetting.DetailSettingFragment;
import com.wadexhong.boxscore.startgame.detailsetting.DetailSettingPresenter;
import com.wadexhong.boxscore.startgame.gamenamesetting.GameNameSettingFragment;
import com.wadexhong.boxscore.startgame.gamenamesetting.GameNameSettingPresenter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.startgame.playerlist.PlayerListFragment;
import com.wadexhong.boxscore.startgame.playerlist.PlayerListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/1.
 */

public class StartGamePresenter implements StartGameContract.Presenter {

    private final StartGameContract.View mStartGameView;

    private FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private GameNameSettingFragment mGameNameSettingFragment;
    private GameNameSettingPresenter mGameNameSettingPresenter;
    private PlayerListFragment mPlayerListFragment;
    private PlayerListPresenter mPlayerListPresenter;
    private DetailSettingFragment mDetailSettingFragment;
    private DetailSettingPresenter mDetailSettingPresenter;
    private List<Fragment> mFragmentList;


    public StartGamePresenter(StartGameContract.View startGameView, FragmentManager manager) {
        mStartGameView = startGameView;
        mFragmentManager = manager;
        mStartGameView.setPresenter(this);

        setViewPager();
    }

    private void setViewPager() {
        mGameNameSettingFragment = GameNameSettingFragment.newInstance();
        mPlayerListFragment = PlayerListFragment.newInstance();
        mDetailSettingFragment = DetailSettingFragment.newInstance();
        mGameNameSettingPresenter = new GameNameSettingPresenter(mGameNameSettingFragment, this);
        mPlayerListPresenter = new PlayerListPresenter(mPlayerListFragment);
        mDetailSettingPresenter = new DetailSettingPresenter(mDetailSettingFragment);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mGameNameSettingFragment);
        mFragmentList.add(mPlayerListFragment);
        mFragmentList.add(mDetailSettingFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        mStartGameView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mStartGameView.setGameNameSettingToolBar();
                break;
            case 1:
                mStartGameView.setPlayerListToolBar();
                break;
            case 2:
                mStartGameView.setDetailSettingToolBar();
                break;
        }
    }

    @Override
    public void transToGameNameSettingPage() {
    }

    @Override
    public void transToPlayerSettingPage() {
    }

    @Override
    public void transToDetailSettingPage() {
    }

    @Override
    public void checkInput(int currentItem) {

        switch (currentItem) {

            case 0:
                mStartGameView.setViewPagerCurrentItem(mGameNameSettingPresenter.checkInputIsLegal());
                break;

            case 1:
                mStartGameView.setViewPagerCurrentItem(mPlayerListPresenter.checkInputIsLegal());
                break;
        }
    }

    @Override
    public void setDefaultPlayerList(String teamId) {
        mPlayerListPresenter.setDefaultPlayerList(teamId);
    }

    @Override
    public GameInfo getSettingResult(GameInfo gameInfo) {

        mGameNameSettingPresenter.getDataFromView(gameInfo);
        mPlayerListPresenter.getDataFromView(gameInfo);
        mDetailSettingPresenter.getDataFromView(gameInfo);

        return gameInfo;
    }

    @Override
    public void start() {

    }
}
