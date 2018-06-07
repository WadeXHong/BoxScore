package com.wadexhong.boxscore.gameboxscore.changeplayer;

import com.wadexhong.boxscore.gameboxscore.changeplayer.changedialog.ChangePlayerDialogPresenter;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreContract;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.gameboxscore.changeplayer.changedialog.ChangePlayerDialog;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/3.
 */

public class ChangePlayerPresenter implements ChangePlayerContract.Presenter {

    private final String TAG = ChangePlayerPresenter.class.getSimpleName();

    private final ChangePlayerContract.View mPlayerOnCourtView;
    private final GameBoxScoreContract.Presenter mGameBoxScorePresenter;

    private GameInfo mGameInfo;
    private ArrayList<Player> mPlayerOnCourtArrayList;
    private ArrayList<Player> mPlayerOnBenchArrayList;
    private ChangePlayerAdapter mAdapter;

    public ChangePlayerPresenter(ChangePlayerContract.View playerOnCourtView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        mPlayerOnCourtView = playerOnCourtView;
        this.mGameBoxScorePresenter = gameBoxScorePresenter;
        mPlayerOnCourtView.setPresenter(this);
    }

    private void getPlayerList() {
        mGameInfo = mGameBoxScorePresenter.getGameInfo();
        mPlayerOnCourtArrayList = mGameInfo.getStartingPlayerList();
        mPlayerOnBenchArrayList = mGameInfo.getSubstitutePlayerList();
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void offGamePlayerSelected(int position) {
        ChangePlayerDialog dialog = ChangePlayerDialog.newInstance(position);
        ChangePlayerDialogPresenter presenter = new ChangePlayerDialogPresenter(dialog, this, position, true);
        mPlayerOnCourtView.popInGamePlayerDialog(dialog);
    }

    @Override
    public void inGamePlayerSelected(int position) {
        ChangePlayerDialog dialog = ChangePlayerDialog.newInstance(position);
        ChangePlayerDialogPresenter presenter = new ChangePlayerDialogPresenter(dialog, this, position, false);
        mPlayerOnCourtView.popOffGamePlayerDialog(dialog);
    }

    @Override
    public void updateFragmentUi() {
        mPlayerOnCourtView.updateUi();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void start() {
        getPlayerList();
        mAdapter = new ChangePlayerAdapter(this, mPlayerOnCourtArrayList, mPlayerOnBenchArrayList);
        mPlayerOnCourtView.setAdapter(mAdapter);
    }
}
