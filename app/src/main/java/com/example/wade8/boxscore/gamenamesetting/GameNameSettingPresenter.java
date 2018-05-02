package com.example.wade8.boxscore.gamenamesetting;

/**
 * Created by wade8 on 2018/5/1.
 */

public class GameNameSettingPresenter implements GameNameSettingContract.Presenter {

    private final GameNameSettingContract.View mGameNameSettingView;

    public GameNameSettingPresenter(GameNameSettingContract.View gameNameSettingView) {
        this.mGameNameSettingView = gameNameSettingView;
    }

    @Override
    public void start() {

    }
}
