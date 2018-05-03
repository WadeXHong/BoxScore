package com.example.wade8.boxscore.gamenamesetting;

import com.example.wade8.boxscore.startgame.StartGameContract;

/**
 * Created by wade8 on 2018/5/1.
 */

public class GameNameSettingPresenter implements GameNameSettingContract.Presenter {

    private final GameNameSettingContract.View mGameNameSettingView;
    private StartGameContract.Presenter mStartGamePresenter;

    public GameNameSettingPresenter(GameNameSettingContract.View gameNameSettingView, StartGameContract.Presenter startGamePresenter) {
        this.mGameNameSettingView = gameNameSettingView;
        mStartGamePresenter = startGamePresenter;
    }

    @Override
    public void start() {

    }
}
