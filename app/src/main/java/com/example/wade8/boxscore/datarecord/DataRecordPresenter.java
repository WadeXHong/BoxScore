package com.example.wade8.boxscore.datarecord;

import android.util.Log;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectPresenter;

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

    private void createPlayerSelectDialog(int type){

        mDataRecordView.enableAllButtons(false);
        if (type == Constants.RecordDataType.TWO_POINT_SHOT || type == Constants.RecordDataType.THREE_POINT_SHOT){
            mDataRecordView.popIsShotMadeDialog(type);

        }else {
            PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
            PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog);
            mDataRecordView.popPlayerSelectDialog(dialog,Constants.mSpareArray.get(type));
        }

    }
    @Override
    public void PressTwoPoint() {
        Log.d(TAG,"PressTwoPint executed");
        createPlayerSelectDialog(Constants.RecordDataType.TWO_POINT_SHOT);
    }

    @Override
    public void PressThreePoint() {
        Log.d(TAG,"PressThreePoint executed");
        createPlayerSelectDialog(Constants.RecordDataType.THREE_POINT_SHOT);
    }

    @Override
    public void PressFreeThrow() {
        Log.d(TAG,"PressFreeThrow executed");
        createPlayerSelectDialog(Constants.RecordDataType.FREE_THROW_SHOT);
    }

    @Override
    public void PressAssist() {
        Log.d(TAG,"PressAssist executed");
        createPlayerSelectDialog(Constants.RecordDataType.ASSIST);
    }

    @Override
    public void PressOffensiveRebound() {
        Log.d(TAG,"PressOffensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.OFFENSIVE_REBOUND);
    }

    @Override
    public void PressSteal() {
        Log.d(TAG,"PressSteal executed");
        createPlayerSelectDialog(Constants.RecordDataType.STEAL);
    }

    @Override
    public void PressBlock() {
        Log.d(TAG,"PressBlock executed");
        createPlayerSelectDialog(Constants.RecordDataType.BLOCK);
    }

    @Override
    public void PressFoul() {
        Log.d(TAG,"PressFoul executed");
        createPlayerSelectDialog(Constants.RecordDataType.FOUL);
    }

    @Override
    public void PressTurnover() {
        Log.d(TAG,"PressTurnover executed");
        createPlayerSelectDialog(Constants.RecordDataType.TURNOVER);
    }

    @Override
    public void PressDefensiveRebound() {
        Log.d(TAG,"PressDefensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.DEFENSIVE_REBOUND);
    }

    @Override
    public void PressShotMade(int type) {
        Log.d(TAG,"PressShotMade executed");
        PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
        PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog);
        mDataRecordView.popPlayerSelectDialog(dialog,Constants.mSpareArray.get(type));
    }

    @Override
    public void PressShotMissed(int type) {
        Log.d(TAG,"PressShotMade executed");
        PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
        PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog);
        mDataRecordView.popPlayerSelectDialog(dialog,Constants.mSpareArray.get(type));
    }
}
