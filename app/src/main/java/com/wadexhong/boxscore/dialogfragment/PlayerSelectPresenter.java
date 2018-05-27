package com.wadexhong.boxscore.dialogfragment;

import com.wadexhong.boxscore.datarecord.DataRecordContract;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectPresenter implements PlayerSelectorContract.Presenter {

    private static final String TAG = PlayerSelectPresenter.class.getSimpleName();

    private final PlayerSelectorContract.View mPlayerSelectView;
    private final DataRecordContract.Presenter mDataRecordPresenter;

    private GameInfo mGameInfo;

    public PlayerSelectPresenter(PlayerSelectorContract.View mPlayerSelectView , DataRecordContract.Presenter mDataRecordPresenter) {
        this.mPlayerSelectView = mPlayerSelectView;
        mPlayerSelectView.setPresenter(this);
        this.mDataRecordPresenter = mDataRecordPresenter;
    }


    @Override
    public void start() {
        mGameInfo = mDataRecordPresenter.getGameInfo();
    }

    @Override
    public void editDataInDB(int position, int type) {
        mDataRecordPresenter.callActivityPresenterEditDataInDb(position, type);
        mPlayerSelectView.dismiss();
    }

    @Override
    public ArrayList<Player> getPlayerOnCourt() {
        return mGameInfo.getStartingPlayerList();
    }
}
