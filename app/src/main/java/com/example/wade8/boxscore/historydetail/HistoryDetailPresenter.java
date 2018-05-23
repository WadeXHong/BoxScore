package com.example.wade8.boxscore.historydetail;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.historyplayersdata.HistoryPlayersDataFragment;
import com.example.wade8.boxscore.historyplayersdata.HistoryPlayersPresenter;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataFragment;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryDetailPresenter implements HistoryDetailContract.Presenter {

    private static final String TAG = HistoryDetailPresenter.class.getSimpleName();

    private final HistoryDetailContract.View mHistoryDetailView;

    private FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private HistoryTeamDataFragment mHistoryTeamDataFragment;
    private HistoryTeamDataPresenter mHistoryTeamDataPresenter;
    private HistoryPlayersDataFragment mHistoryPlayersDataFragment;
    private HistoryPlayersPresenter mHistoryPlayersPresenter;
    private List<Fragment> mFragmentList;
    private String mGameId;

    public HistoryDetailPresenter(HistoryDetailContract.View historyDetailView, FragmentManager manager) {
        mHistoryDetailView = historyDetailView;
        mFragmentManager = manager;

        mHistoryDetailView.setPresenter(this);

    }

    private void setViewPager() {

        mHistoryTeamDataFragment = HistoryTeamDataFragment.newInstance(mGameId);
        mHistoryTeamDataPresenter = new HistoryTeamDataPresenter(mHistoryTeamDataFragment);
        mHistoryPlayersDataFragment = HistoryPlayersDataFragment.newInstance();
        mHistoryPlayersPresenter = new HistoryPlayersPresenter(mHistoryPlayersDataFragment);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHistoryTeamDataFragment);
        mFragmentList.add(mHistoryPlayersDataFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        mHistoryDetailView.setViewPagerAdapter(mViewPagerFragmentAdapter);

    }

    @Override
    public void start() {
        setViewPager();
    }

    @Override
    public void refreshUi() {
        mHistoryDetailView.refreshUi();
        mHistoryTeamDataPresenter.refreshUi(mGameId);
        mHistoryPlayersPresenter.refreshUi(mGameId);
    }

    public void setGameId(String gameId) {
        mGameId = gameId;
    }
}
