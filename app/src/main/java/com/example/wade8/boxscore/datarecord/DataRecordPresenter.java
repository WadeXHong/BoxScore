package com.example.wade8.boxscore.datarecord;

import android.util.Log;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectPresenter;
import com.example.wade8.boxscore.gameboxscore.GameBoxScoreContract;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataRecordPresenter implements DataRecordContract.Presenter{

    private static final String TAG = DataRecordPresenter.class.getSimpleName();

    private final DataRecordContract.View mDataRecordView;
    private final GameBoxScoreContract.Presenter mPresenter;

    private GameInfo mGameInfo;

    public DataRecordPresenter(DataRecordContract.View dataRecordView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        mDataRecordView = dataRecordView;
        mDataRecordView.setPresenter(this);
        mPresenter = gameBoxScorePresenter;
    }

    @Override
    public void start() {
        mGameInfo = mPresenter.getGameInfo();
    }

    private void createPlayerSelectDialog(int type){

        mDataRecordView.enableAllButtons(false);
        if (type == Constants.RecordDataType.TWO_POINT_SHOT || type == Constants.RecordDataType.THREE_POINT_SHOT || type == Constants.RecordDataType.FREE_THROW_SHOT){
            mDataRecordView.popIsShotMadeDialog(type);

        }else {
            PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
            PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog,this);
            mDataRecordView.popPlayerSelectDialog(dialog,Constants.TITLE_SPARSE_ARRAY.get(type));
        }

    }
    @Override
    public void pressTwoPoint() {
        Log.d(TAG,"PressTwoPint executed");
        createPlayerSelectDialog(Constants.RecordDataType.TWO_POINT_SHOT);
    }

    @Override
    public void pressThreePoint() {
        Log.d(TAG,"pressThreePoint executed");
        createPlayerSelectDialog(Constants.RecordDataType.THREE_POINT_SHOT);
    }

    @Override
    public void pressFreeThrow() {
        Log.d(TAG,"pressFreeThrow executed");
        createPlayerSelectDialog(Constants.RecordDataType.FREE_THROW_SHOT);
    }

    @Override
    public void pressAssist() {
        Log.d(TAG,"pressAssist executed");
        createPlayerSelectDialog(Constants.RecordDataType.ASSIST);
    }

    @Override
    public void pressOffensiveRebound() {
        Log.d(TAG,"pressOffensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.OFFENSIVE_REBOUND);
    }

    @Override
    public void pressSteal() {
        Log.d(TAG,"pressSteal executed");
        createPlayerSelectDialog(Constants.RecordDataType.STEAL);
    }

    @Override
    public void pressBlock() {
        Log.d(TAG,"pressBlock executed");
        createPlayerSelectDialog(Constants.RecordDataType.BLOCK);
    }

    @Override
    public void pressFoul() {
        Log.d(TAG,"pressFoul executed");
        createPlayerSelectDialog(Constants.RecordDataType.FOUL);
    }

    @Override
    public void pressTurnover() {
        Log.d(TAG,"pressTurnover executed");
        createPlayerSelectDialog(Constants.RecordDataType.TURNOVER);
    }

    @Override
    public void pressDefensiveRebound() {
        Log.d(TAG,"pressDefensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.DEFENSIVE_REBOUND);
    }

    @Override
    public void pressShotMade(int type) {
        Log.d(TAG,"pressShotMade executed");
        PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
        PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog,this);
        mDataRecordView.popPlayerSelectDialog(dialog,Constants.TITLE_SPARSE_ARRAY.get(type));
    }

    @Override
    public void pressShotMissed(int type) {
        Log.d(TAG,"pressShotMade executed");
        PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
        PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog,this);
        mDataRecordView.popPlayerSelectDialog(dialog,Constants.TITLE_SPARSE_ARRAY.get(type));
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }
}
