package com.example.wade8.boxscore.datarecord;

import android.util.Log;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataRecordPresenter implements DataRecordContract.Presenter{

    private static final String TAG = DataRecordPresenter.class.getSimpleName();

    private final DataRecordContract.View mDataRecordView;

    public DataRecordPresenter(DataRecordContract.View dataRecordView) {
        mDataRecordView = dataRecordView;
        mDataRecordView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void PressTwoPoint() {
        Log.d(TAG,"PressTwoPint executed");
    }

    @Override
    public void PressThreePoint() {
        Log.d(TAG,"PressThreePoint executed");
    }

    @Override
    public void PressFreeThrow() {
        Log.d(TAG,"PressFreeThrow executed");
    }

    @Override
    public void PressAssist() {
        Log.d(TAG,"PressAssist executed");
    }

    @Override
    public void PressOffensiveRebound() {
        Log.d(TAG,"PressOffensiveRebound executed");
    }

    @Override
    public void PressSteal() {
        Log.d(TAG,"PressSteal executed");
    }

    @Override
    public void PressBlock() {
        Log.d(TAG,"PressBlock executed");
    }

    @Override
    public void PressFoul() {
        Log.d(TAG,"PressFoul executed");
    }

    @Override
    public void PressTurnover() {
        Log.d(TAG,"PressTurnover executed");
    }

    @Override
    public void PressDefensiveRebound() {
        Log.d(TAG,"PressDefensiveRebound executed");
    }
}
