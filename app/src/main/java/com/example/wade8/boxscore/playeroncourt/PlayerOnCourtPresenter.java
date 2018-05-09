package com.example.wade8.boxscore.playeroncourt;

import com.example.wade8.boxscore.adapter.PlayerOnCourtAdapter;
import com.example.wade8.boxscore.gameboxscore.GameBoxScoreContract;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/3.
 */

public class PlayerOnCourtPresenter implements PlayerOnCourtContract.Presenter{

    private static final String TAG = PlayerOnCourtPresenter.class.getSimpleName();

    private final PlayerOnCourtContract.View mPlayerOnCourtView;
    private final GameBoxScoreContract.Presenter mGameBoxScorePresenter;

    private ArrayList<Player> mPlayerOnCourtList;
    private ArrayList<Player> mPlayerOnBenchList;
    private PlayerOnCourtAdapter mAdapter;

    public PlayerOnCourtPresenter(PlayerOnCourtContract.View playerOnCourtView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        mPlayerOnCourtView = playerOnCourtView;
        this.mGameBoxScorePresenter = gameBoxScorePresenter;
        mPlayerOnCourtView.setPresenter(this);
    }

    @Override
    public void start() {
        getPlayerList();
        mAdapter = new PlayerOnCourtAdapter(mPlayerOnCourtList,mPlayerOnBenchList);
        mPlayerOnCourtView.setAdapter(mAdapter);
    }

    private void getPlayerList() {
        mPlayerOnCourtList = mGameBoxScorePresenter.getGameInfo().getStartingPlayerList();
        mPlayerOnBenchList = mGameBoxScorePresenter.getGameInfo().getSubstitutePlayerList();
    }
}
