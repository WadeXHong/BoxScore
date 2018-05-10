package com.example.wade8.boxscore.playeroncourt;

import com.example.wade8.boxscore.adapter.ChangePlayerAdapter;
import com.example.wade8.boxscore.gameboxscore.GameBoxScoreContract;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;
import com.example.wade8.boxscore.playeroncourt.changedialog.ChangePlayerDialog;
import com.example.wade8.boxscore.playeroncourt.changedialog.ChangePlayerDialogPresenter;

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
