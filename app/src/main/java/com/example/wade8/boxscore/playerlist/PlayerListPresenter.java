package com.example.wade8.boxscore.playerlist;

import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public class PlayerListPresenter implements PlayerListContract.Presenter{

    private static final String TAG = PlayerListPresenter.class.getSimpleName();

    private final PlayerListContract.View mPlayerListView;

    public PlayerListPresenter(PlayerListContract.View playerListView) {
        this.mPlayerListView = playerListView;
    }

    @Override
    public void start() {

    }

    public void getDataFromView(GameInfo gameInfo) {
        mPlayerListView.getSettingResult(gameInfo);
    }
}
