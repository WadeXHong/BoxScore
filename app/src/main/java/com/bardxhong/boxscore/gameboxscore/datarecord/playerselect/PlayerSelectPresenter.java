package com.bardxhong.boxscore.gameboxscore.datarecord.playerselect;

import com.bardxhong.boxscore.gameboxscore.datarecord.DataRecordContract;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectPresenter implements PlayerSelectContract.Presenter {

    private static final String TAG = PlayerSelectPresenter.class.getSimpleName();

    private final PlayerSelectContract.View mPlayerSelectView;
    private final DataRecordContract.Presenter mDataRecordPresenter;

    private GameInfo mGameInfo;

    @Override
    public ArrayList<Player> getPlayerOnCourt() {
        return mGameInfo.getStartingPlayerList();
    }


    public PlayerSelectPresenter(PlayerSelectContract.View playerSelectView, DataRecordContract.Presenter dataRecordPresenter) {
        mPlayerSelectView = playerSelectView;
        playerSelectView.setPresenter(this);
        mDataRecordPresenter = dataRecordPresenter;
    }

    @Override
    public void editDataInDB(Player player, int type) {
        mDataRecordPresenter.callActivityPresenterEditDataInDb(player, type);
        mPlayerSelectView.dismiss();
    }

    @Override
    public void start() {
        mGameInfo = mDataRecordPresenter.getGameInfo();
    }
}
