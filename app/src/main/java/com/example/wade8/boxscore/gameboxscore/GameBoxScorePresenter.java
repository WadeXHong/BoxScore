package com.example.wade8.boxscore.gameboxscore;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
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

    private static final String TAG = GameBoxScorePresenter.class.getSimpleName();

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
    private SparseArray<Integer> mTeamData;
    private GameInfo mGameInfo;


    public GameBoxScorePresenter(GameBoxScoreContract.View mGameBoxScoreView, android.support.v4.app.FragmentManager manager) {
        this.mGameBoxScoreView = mGameBoxScoreView;
        mFragmentManager = manager;
        mGameBoxScoreView.setPresenter(this);
        initTeamData();
        setViewPager();
    }

    private void initTeamData() {
        mTeamData = new SparseArray<>();
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.QUARTER,1);
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
        mGameBoxScoreView.setInitDataOnScreen(mTeamData);
        mGameInfo = mGameBoxScoreView.getGameInfo();
    }

    @Override
    public void writeInitDataIntoDataBase() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.writeInitDataIntoDataBase(mGameInfo);
    }

    @Override
    public void pressOpponentTeamScore(String score) {
        Log.d(TAG,"Opponent Score +1");
        mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,Integer.parseInt(score)+1);
        mGameBoxScoreView.updateUiTeamData();
    }


    @Override
    public void pressYourTeamFoul(String foul) {
        if (foul.equals(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.YOUR_TEAM_FOUL,Integer.parseInt(foul)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressOpponentTeamFoul(String foul) {
        if (foul.equals(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL,Integer.parseInt(foul)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressQuarter(String quarter) {
        if (quarter.equals(mGameInfo.getTotalQuarter())){
            Log.d(TAG,"Quarter is already GG");
        }else{
            mTeamData.put(Constants.RecordDataType.QUARTER,Integer.parseInt(quarter)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }
}
