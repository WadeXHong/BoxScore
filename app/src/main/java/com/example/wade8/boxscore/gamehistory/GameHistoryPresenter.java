package com.example.wade8.boxscore.gamehistory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.historydetail.HistoryDetailFragment;
import com.example.wade8.boxscore.historydetail.HistoryDetailPresenter;
import com.example.wade8.boxscore.historymain.HistoryMainFragment;
import com.example.wade8.boxscore.historymain.HistoryMainPresenter;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataFragment;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataPresenter;

/**
 * Created by wade8 on 2018/5/22.
 */

public class GameHistoryPresenter implements GameHistoryContract.Presenter{

    private static final String TAG = GameHistoryPresenter.class.getSimpleName();

    public static final String HISTORY_MAIN = "HISTORY_MAIN";
    public static final String HISTORY_DETAIL = "HISTORY_DETAIL";


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


    @Override
    public void start() {
        transToMain();
    }

    @Override
    public void transToHistory() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHistoryMainFragment == null) mHistoryMainFragment = HistoryMainFragment.newInstance();
        if (mHistoryDetailFragment != null) transaction.remove(mHistoryDetailFragment);
        if (!mHistoryMainFragment.isAdded()){
            transaction.add(R.id.activity_gamehistory_framelayout, mHistoryMainFragment, HISTORY_MAIN);
        }else {
            transaction.show(mHistoryMainFragment);
        }
        transaction.commit();

        if (mHistoryMainPresenter == null) mHistoryMainPresenter = new HistoryMainPresenter(mHistoryMainFragment);

        mGameHistoryView.setGameHistoryToolBar();
    }

    @Override
    public void transToMain() {

    }
}
