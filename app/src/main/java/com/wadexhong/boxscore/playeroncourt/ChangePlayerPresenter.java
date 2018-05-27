package com.wadexhong.boxscore.playeroncourt;

import com.wadexhong.boxscore.playeroncourt.changedialog.ChangePlayerDialogPresenter;
import com.wadexhong.boxscore.adapter.ChangePlayerAdapter;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreContract;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.playeroncourt.changedialog.ChangePlayerDialog;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/3.
 */

public class ChangePlayerPresenter implements ChangePlayerContract.Presenter{

    private static final String TAG = ChangePlayerPresenter.class.getSimpleName();

    private final ChangePlayerContract.View mPlayerOnCourtView;
    private final GameBoxScoreContract.Presenter mGameBoxScorePresenter;

    private GameInfo mGameInfo;
    private ArrayList<Player> mPlayerOnCourtList;
    private ArrayList<Player> mPlayerOnBenchList;
    private ChangePlayerAdapter mAdapter;

    public ChangePlayerPresenter(ChangePlayerContract.View playerOnCourtView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        mPlayerOnCourtView = playerOnCourtView;
        this.mGameBoxScorePresenter = gameBoxScorePresenter;
        mPlayerOnCourtView.setPresenter(this);
    }

    @Override
    public void start() {
        getPlayerList();
        mAdapter = new ChangePlayerAdapter(this, mPlayerOnCourtList,mPlayerOnBenchList);
        mPlayerOnCourtView.setAdapter(mAdapter);
    }

    private void getPlayerList() {
        mGameInfo = mGameBoxScorePresenter.getGameInfo();
        mPlayerOnCourtList = mGameInfo.getStartingPlayerList();
        mPlayerOnBenchList = mGameInfo.getSubstitutePlayerList();
    }

    @Override
    public void offGamePlayerSelected(int position) {
        ChangePlayerDialog dialog = ChangePlayerDialog.newInstance(position);
        ChangePlayerDialogPresenter presenter = new ChangePlayerDialogPresenter(dialog,this, position, true);
        mPlayerOnCourtView.popInGamePlayerDialog(dialog);
    }

    @Override
    public void inGamePlayerSelected(int position) {
        ChangePlayerDialog dialog = ChangePlayerDialog.newInstance(position);
        ChangePlayerDialogPresenter presenter = new ChangePlayerDialogPresenter(dialog,this, position, false);
        mPlayerOnCourtView.popOffGamePlayerDialog(dialog);
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void updateFragmentUi() {
        mPlayerOnCourtView.updateUi();
        mAdapter.notifyDataSetChanged();
    }
}
