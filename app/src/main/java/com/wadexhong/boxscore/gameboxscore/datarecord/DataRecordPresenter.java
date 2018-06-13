package com.wadexhong.boxscore.gameboxscore.datarecord;

import android.util.Log;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.gameboxscore.datarecord.playerselect.PlayerSelectDialog;
import com.wadexhong.boxscore.gameboxscore.datarecord.playerselect.PlayerSelectPresenter;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreContract;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataRecordPresenter implements DataRecordContract.Presenter {

    private final String TAG = DataRecordPresenter.class.getSimpleName();

    private final DataRecordContract.View mDataRecordView;
    private final GameBoxScoreContract.Presenter mGamBoxScorePresenter;

    private GameInfo mGameInfo;

    public DataRecordPresenter(DataRecordContract.View dataRecordView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        mDataRecordView = dataRecordView;
        mDataRecordView.setPresenter(this);
        mGamBoxScorePresenter = gameBoxScorePresenter;
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void pressTwoPoint() {
        Log.d(TAG, "PressTwoPint executed");
        createPlayerSelectDialog(Constants.RecordDataType.TWO_POINT_SHOT);
    }

    @Override
    public void pressTwoPointMade() {
        Log.d(TAG, "pressTwoPointMade executed");
        createPlayerSelectDialog(Constants.RecordDataType.TWO_POINT_SHOT_MADE);
    }

    @Override
    public void pressTwoPointMissed() {
        Log.d(TAG, "pressTwoPointMissed executed");
        createPlayerSelectDialog(Constants.RecordDataType.TWO_POINT_SHOT_MISSED);
    }

    @Override
    public void pressThreePoint() {
        Log.d(TAG, "pressThreePoint executed");
        createPlayerSelectDialog(Constants.RecordDataType.THREE_POINT_SHOT);
    }

    @Override
    public void pressThreePointMade() {
        Log.d(TAG, "pressThreePointMade executed");
        createPlayerSelectDialog(Constants.RecordDataType.THREE_POINT_SHOT_MADE);
    }

    @Override
    public void pressThreePointMissed() {
        Log.d(TAG, "pressThreePointMissed executed");
        createPlayerSelectDialog(Constants.RecordDataType.THREE_POINT_SHOT_MISSED);
    }

    @Override
    public void pressFreeThrow() {
        Log.d(TAG, "pressFreeThrow executed");
        createPlayerSelectDialog(Constants.RecordDataType.FREE_THROW_SHOT);
    }

    @Override
    public void pressFreeThrowMade() {
        Log.d(TAG, "pressFreeThrowMade executed");
        createPlayerSelectDialog(Constants.RecordDataType.FREE_THROW_SHOT_MADE);
    }

    @Override
    public void pressFreeThrowMissed() {
        Log.d(TAG, "pressFreeThrowMissed executed");
        createPlayerSelectDialog(Constants.RecordDataType.FREE_THROW_SHOT_MISSED);
    }

    @Override
    public void pressAssist() {
        Log.d(TAG, "pressAssist executed");
        createPlayerSelectDialog(Constants.RecordDataType.ASSIST);
    }

    @Override
    public void pressOffensiveRebound() {
        Log.d(TAG, "pressOffensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.OFFENSIVE_REBOUND);
    }

    @Override
    public void pressSteal() {
        Log.d(TAG, "pressSteal executed");
        createPlayerSelectDialog(Constants.RecordDataType.STEAL);
    }

    @Override
    public void pressBlock() {
        Log.d(TAG, "pressBlock executed");
        createPlayerSelectDialog(Constants.RecordDataType.BLOCK);
    }

    @Override
    public void pressFoul() {
        Log.d(TAG, "pressFoul executed");
        createPlayerSelectDialog(Constants.RecordDataType.FOUL);
    }

    @Override
    public void pressTurnover() {
        Log.d(TAG, "pressTurnover executed");
        createPlayerSelectDialog(Constants.RecordDataType.TURNOVER);
    }

    @Override
    public void pressDefensiveRebound() {
        Log.d(TAG, "pressDefensiveRebound executed");
        createPlayerSelectDialog(Constants.RecordDataType.DEFENSIVE_REBOUND);
    }

    @Override
    public void pressShotMade(int type) {
        Log.d(TAG, "pressShotMade executed");
        popPlayerSelectProcess(type);
    }

    @Override
    public void pressShotMissed(int type) {
        Log.d(TAG, "pressShotMade executed");
        popPlayerSelectProcess(type);
    }

    @Override
    public void pressOffensiveFoul(int type) {
        Log.d(TAG, "pressOffensiveFoul executed");
        popPlayerSelectProcess(type);
    }

    @Override
    public void pressDefensiveFoul(int type) {
        Log.d(TAG, "pressDefensiveFoul executed");
        popPlayerSelectProcess(type);
    }

    private void createPlayerSelectDialog(int type) {
        //TODO Foul
        mDataRecordView.enableAllButtons(false);

        if (type == Constants.RecordDataType.TWO_POINT_SHOT || type == Constants.RecordDataType.THREE_POINT_SHOT || type == Constants.RecordDataType.FREE_THROW_SHOT) {
            mDataRecordView.popIsShotMadeDialog(type);
        } else if (type == Constants.RecordDataType.FOUL) {
            mDataRecordView.popFoulTypeDialog(type);
        } else {
            popPlayerSelectProcess(type);
        }

    }

    public void popPlayerSelectProcess(int type) {
        PlayerSelectDialog dialog = PlayerSelectDialog.newInstance(type);
        PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog, this);
        mDataRecordView.popPlayerSelectDialog(dialog, Constants.TITLE_SPARSE_ARRAY.get(type));
    }

    @Override
    public void callActivityPresenterEditDataInDb(Player player, int type) {
        mGamBoxScorePresenter.editDataInDb(player, type);
    }

    @Override
    public void updateUiGameBoxActivity() {
        mGamBoxScorePresenter.updateUi();
    }

    @Override
    public void start() {
        mGameInfo = mGamBoxScorePresenter.getGameInfo();
    }
}
