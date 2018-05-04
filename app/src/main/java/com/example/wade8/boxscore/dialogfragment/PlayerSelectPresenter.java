package com.example.wade8.boxscore.dialogfragment;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectPresenter implements PlayerSelecterContract.Presenter {

    private static final String TAG = PlayerSelectPresenter.class.getSimpleName();

    private final PlayerSelecterContract.View mPlayerSelectView;

    public PlayerSelectPresenter(PlayerSelecterContract.View mPlayerSelectView) {
        this.mPlayerSelectView = mPlayerSelectView;
        mPlayerSelectView.setPresenter(this);
    }


    @Override
    public void start() {

    }
}
