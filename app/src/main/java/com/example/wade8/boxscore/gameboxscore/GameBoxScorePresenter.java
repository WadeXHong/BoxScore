package com.example.wade8.boxscore.gameboxscore;

import android.app.FragmentManager;

import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameBoxScorePresenter implements GameBoxScoreContract.Presenter{

    private final GameBoxScoreContract.View mGameBoxScoreView;

    private FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;

    public GameBoxScorePresenter(GameBoxScoreContract.View mGameBoxScoreView) {
        this.mGameBoxScoreView = mGameBoxScoreView;
    }


    @Override
    public void start() {

    }
}
