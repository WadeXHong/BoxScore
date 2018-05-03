package com.example.wade8.boxscore.startgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.detailsetting.DetailSettingFragment;
import com.example.wade8.boxscore.detailsetting.DetailSettingPresenter;
import com.example.wade8.boxscore.gamenamesetting.GameNameSettingFragment;
import com.example.wade8.boxscore.gamenamesetting.GameNameSettingPresenter;
import com.example.wade8.boxscore.playerlist.PlayerListFragment;
import com.example.wade8.boxscore.playerlist.PlayerListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/1.
 */

public class StartGamePresenter implements StartGameContract.Presenter{

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
        this.mStartGameView = startGameView;
        mFragmentManager = manager;
        mStartGameView.setPresenter(this);

        setViewPager();
    }

    private void setViewPager(){
        mGameNameSettingFragment = GameNameSettingFragment.newInstance();
        mPlayerListFragment = PlayerListFragment.newInstance();
        mDetailSettingFragment = DetailSettingFragment.newInstance();
        mDetailSettingPresenter = new DetailSettingPresenter(mDetailSettingFragment);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mGameNameSettingFragment);
        mFragmentList.add(mPlayerListFragment);
        mFragmentList.add(mDetailSettingFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        mStartGameView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }

    @Override
    public void start() {

    }

    @Override
    public void getTeamFromFireBase() {

    }
}
