package com.bardxhong.boxscore.gameboxscore.changeplayer.changedialog;

import com.bardxhong.boxscore.gameboxscore.changeplayer.ChangePlayerContract;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class ChangePlayerDialogPresenter implements ChangePlayerDialogContract.Presenter {

    private static final String TAG = ChangePlayerDialogPresenter.class.getSimpleName();

    private final ChangePlayerDialogContract.View mChangePlayerDialogView;
    private final ChangePlayerContract.Presenter mChangePlayerPresenter;

    private ChangePlayerDialogAdapter mAdapter;


    private GameInfo mGameInfo;
    private ArrayList<Player> mPlayerOnCourtList;
    private ArrayList<Player> mPlayerOnBenchList;
    private int mPositionInput;
    private boolean mIsRequestOnCourtPlayer;

    /**
     * @param changePlayerDialogView Binded View of this presenter
     * @param changePlayerPresenter  Interface of parent presenter
     * @param position               Position in selected position
     * @param isRequestOnCourtPlayer Clicked player is on court or on bench
     */
    public ChangePlayerDialogPresenter(ChangePlayerDialogContract.View changePlayerDialogView, ChangePlayerContract.Presenter changePlayerPresenter, int position, boolean isRequestOnCourtPlayer) {
        mChangePlayerDialogView = changePlayerDialogView;
        mChangePlayerPresenter = changePlayerPresenter;
        mPositionInput = position;
        mIsRequestOnCourtPlayer = isRequestOnCourtPlayer;
        changePlayerDialogView.setPresenter(this);
    }


    @Override
    public void initAdapter() {

        mPlayerOnCourtList = mGameInfo.getStartingPlayerList();
        mPlayerOnBenchList = mGameInfo.getSubstitutePlayerList();

        if (mIsRequestOnCourtPlayer) {

            mAdapter = new ChangePlayerDialogAdapter(this, mPlayerOnCourtList);

        } else {

            mAdapter = new ChangePlayerDialogAdapter(this, mPlayerOnBenchList);

        }

        mChangePlayerDialogView.setRecyclerView(mAdapter);
    }


    @Override
    public void exchangePlayer(int layoutPosition) {
        if (mIsRequestOnCourtPlayer) {
            //boolean = true  mPositionInput = 場下position  layoutPosition = 場上準備下場position
            mPlayerOnCourtList.add(mPlayerOnCourtList.get(layoutPosition));
            mPlayerOnCourtList.set(layoutPosition, mPlayerOnBenchList.get(mPositionInput));
            mPlayerOnBenchList.set(mPositionInput, mPlayerOnCourtList.get(mPlayerOnCourtList.size() - 1));
            mPlayerOnCourtList.remove(mPlayerOnCourtList.size() - 1);
        } else {
            //boolean = false  mPositionInput = 場上position  layoutPosition = 場下準備上場的position
            mPlayerOnCourtList.add(mPlayerOnCourtList.get(mPositionInput));
            mPlayerOnCourtList.set(mPositionInput, mPlayerOnBenchList.get(layoutPosition));
            mPlayerOnBenchList.set(layoutPosition, mPlayerOnCourtList.get(mPlayerOnCourtList.size() - 1));
            mPlayerOnCourtList.remove(mPlayerOnCourtList.size() - 1);
        }

        mChangePlayerDialogView.dismiss();
        mChangePlayerPresenter.updateFragmentUi();
    }

    @Override
    public void start() {
        mGameInfo = mChangePlayerPresenter.getGameInfo();
    }
}
