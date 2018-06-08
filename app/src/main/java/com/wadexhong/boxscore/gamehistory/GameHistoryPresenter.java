package com.wadexhong.boxscore.gamehistory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gamehistory.historydetail.HistoryDetailPresenter;
import com.wadexhong.boxscore.gamehistory.historymain.HistoryMainFragment;
import com.wadexhong.boxscore.gamehistory.historydetail.HistoryDetailFragment;
import com.wadexhong.boxscore.gamehistory.historymain.HistoryMainPresenter;

/**
 * Created by wade8 on 2018/5/22.
 */

public class GameHistoryPresenter implements GameHistoryContract.Presenter {

    private final String TAG = GameHistoryPresenter.class.getSimpleName();

    private static final String HISTORY_MAIN = "HISTORY_MAIN";
    private static final String HISTORY_DETAIL = "HISTORY_DETAIL";

    private final GameHistoryContract.View mGameHistoryView;

    private FragmentManager mFragmentManager;
    private HistoryMainFragment mHistoryMainFragment;
    private HistoryMainPresenter mHistoryMainPresenter;
    private HistoryDetailFragment mHistoryDetailFragment;
    private HistoryDetailPresenter mHistoryDetailPresenter;


    public GameHistoryPresenter(GameHistoryContract.View gameHistoryView, FragmentManager supportFragmentManager) {
        mGameHistoryView = gameHistoryView;
        mFragmentManager = supportFragmentManager;

        mGameHistoryView.setPresenter(this);
    }


    private void fragmentsInit() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        mHistoryDetailFragment = HistoryDetailFragment.newInstance();
        mHistoryDetailPresenter = new HistoryDetailPresenter(mHistoryDetailFragment, mFragmentManager);
        mHistoryMainFragment = HistoryMainFragment.newInstance();
        mHistoryMainPresenter = new HistoryMainPresenter(mHistoryMainFragment, this);

        transaction.add(R.id.activity_gamehistory_framelayout, mHistoryDetailFragment, HISTORY_DETAIL);
        transaction.add(R.id.activity_gamehistory_framelayout, mHistoryMainFragment, HISTORY_MAIN);
        transaction.hide(mHistoryDetailFragment);
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        transaction.commit();
    }

    @Override
    public void transToDetail(String gameId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);

        mHistoryDetailPresenter.setGameIdToAdapter(gameId);

        transaction.show(mHistoryDetailFragment);
        transaction.hide(mHistoryMainFragment);
        transaction.addToBackStack(HISTORY_MAIN);
        transaction.commit();

        mGameHistoryView.setHistoryDetailToolBar();
    }

    @Override
    public void transToMain() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        transaction.show(mHistoryMainFragment);
        transaction.hide(mHistoryDetailFragment);
        transaction.commit();

        mGameHistoryView.setGameHistoryToolBar();
    }

    @Override
    public void setGameHistoryToolBar() {
        mGameHistoryView.setGameHistoryToolBar();
    }

    @Override
    public void setHistoryDetailToolBar() {
        mGameHistoryView.setGameHistoryToolBar();
    }

    @Override
    public void start() {
        fragmentsInit();
    }
}
