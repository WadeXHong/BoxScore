package com.example.wade8.boxscore.gameboxscore;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.datarecord.DataRecordFragment;
import com.example.wade8.boxscore.datarecord.DataRecordPresenter;
import com.example.wade8.boxscore.datastatistic.DataStatisticFragment;
import com.example.wade8.boxscore.datastatistic.DataStatisticPresenter;
import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.playeroncourt.PlayerOnCourtFragment;
import com.example.wade8.boxscore.playeroncourt.PlayerOnCourtPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameBoxScorePresenter implements GameBoxScoreContract.Presenter{

    private final GameBoxScoreContract.View mGameBoxScoreView;

    private android.support.v4.app.FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private DataRecordFragment mDataRecordFragment;
    private DataRecordPresenter mDataRecordPresenter;
    private PlayerOnCourtFragment mPlayerOnCourtFragment;
    private PlayerOnCourtPresenter mPlayerOnCourtPresenter;
    private DataStatisticFragment mDataStatisticFragment;
    private DataStatisticPresenter mDataStatisticPresenter;
    private List<Fragment> mFragmentList;


    public GameBoxScorePresenter(GameBoxScoreContract.View mGameBoxScoreView, android.support.v4.app.FragmentManager manager) {
        this.mGameBoxScoreView = mGameBoxScoreView;
        mFragmentManager = manager;
        mGameBoxScoreView.setPresenter(this);

        setViewPager();
    }

    private void setViewPager(){
        mPlayerOnCourtFragment = PlayerOnCourtFragment.newInstance();
        mDataRecordFragment = DataRecordFragment.newInstance();
        mDataStatisticFragment = DataStatisticFragment.newInstance();
        mPlayerOnCourtPresenter = new PlayerOnCourtPresenter(mPlayerOnCourtFragment);
        mDataRecordPresenter = new DataRecordPresenter(mDataRecordFragment);
        mDataStatisticPresenter = new DataStatisticPresenter(mDataStatisticFragment);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mDataRecordFragment);
        mFragmentList.add(mPlayerOnCourtFragment);
        mFragmentList.add(mDataStatisticFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        mGameBoxScoreView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }


    @Override
    public void start() {

    }

    @Override
    public void writeInitDataIntoDataBase(GameInfo gameInfo) {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.writeInitDataIntoDataBase(gameInfo);
    }
}
