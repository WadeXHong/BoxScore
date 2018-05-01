package com.example.wade8.boxscore.startgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.detailsetting.DetailSettingFragment;
import com.example.wade8.boxscore.gamenamesetting.GameNameSettingFragment;
import com.example.wade8.boxscore.playerlist.PlayerListFragment;

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
    private PlayerListFragment mPlayerListFragment;
    private DetailSettingFragment mDetailSettingFragment;
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
