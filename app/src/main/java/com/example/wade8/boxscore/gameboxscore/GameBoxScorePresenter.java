package com.example.wade8.boxscore.gameboxscore;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.datarecord.DataRecordFragment;
import com.example.wade8.boxscore.datarecord.DataRecordPresenter;
import com.example.wade8.boxscore.datastatistic.DataStatisticFragment;
import com.example.wade8.boxscore.datastatistic.DataStatisticPresenter;
import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;
import com.example.wade8.boxscore.dialogfragment.datastatistic.DataStatisticDialog;
import com.example.wade8.boxscore.dialogfragment.datastatistic.DataStatisticDialogPresenter;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Undo;
import com.example.wade8.boxscore.playeroncourt.ChangePlayerFragment;
import com.example.wade8.boxscore.playeroncourt.ChangePlayerPresenter;
import com.example.wade8.boxscore.undohistory.UndoHistoryFragment;
import com.example.wade8.boxscore.undohistory.UndoHistoryPresenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameBoxScorePresenter implements GameBoxScoreContract.Presenter{

    private static final String TAG = GameBoxScorePresenter.class.getSimpleName();

    private final GameBoxScoreContract.View mGameBoxScoreView;

    private android.support.v4.app.FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private DataRecordFragment mDataRecordFragment;
    private DataRecordPresenter mDataRecordPresenter;
    private ChangePlayerFragment mChangePlayerFragment;
    private ChangePlayerPresenter mChangePlayerPresenter;
//    private DataStatisticFragment mDataStatisticFragment;
//    private DataStatisticPresenter mDataStatisticPresenter;
    private UndoHistoryFragment mUndoHistoryFragment;
    private UndoHistoryPresenter mUndoHistoryPresenter;
    private List<Fragment> mFragmentList;
    private SparseIntArray mTeamData;
    private GameInfo mGameInfo;
    private LinkedList<Undo> mUndoList;


    public GameBoxScorePresenter(GameBoxScoreContract.View mGameBoxScoreView, android.support.v4.app.FragmentManager manager) {
        this.mGameBoxScoreView = mGameBoxScoreView;
        mFragmentManager = manager;
        mGameBoxScoreView.setPresenter(this);
        initTeamData();
        setViewPager();
    }

    private void initTeamData() {
        mTeamData = new SparseIntArray();
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.QUARTER,1);

    }

    private void setViewPager(){
        mChangePlayerFragment = ChangePlayerFragment.newInstance();
        mDataRecordFragment = DataRecordFragment.newInstance();
//        mDataStatisticFragment = DataStatisticFragment.newInstance();
        mUndoHistoryFragment = UndoHistoryFragment.newInstance();
        mChangePlayerPresenter = new ChangePlayerPresenter(mChangePlayerFragment, this);
        mDataRecordPresenter = new DataRecordPresenter(mDataRecordFragment,this);
//        mDataStatisticPresenter = new DataStatisticPresenter(mDataStatisticFragment);
        mUndoHistoryPresenter = new UndoHistoryPresenter(mUndoHistoryFragment,this);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mDataRecordFragment);
        mFragmentList.add(mChangePlayerFragment);
//        mFragmentList.add(mDataStatisticFragment);
        mFragmentList.add(mUndoHistoryFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        mGameBoxScoreView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }


    @Override
    public void start() {
        mGameBoxScoreView.setInitDataOnScreen(mTeamData);
        mGameInfo = mGameBoxScoreView.getGameInfo();
        mGameInfo.setTeamData(mTeamData);
        mUndoList = new LinkedList<Undo>();
        writeInitDataIntoModel();
    }

    @Override
    public void writeInitDataIntoModel() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);
        mGameDataDbHelper.setUndoList(mUndoList);
        mGameDataDbHelper.writeInitDataIntoGameInfo();
        mGameDataDbHelper.writeInitDataIntoDataBase();

    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void pressOpponentTeamScore() {
        Log.d(TAG,"Opponent Score +1");
        mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE)+1);
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void pressDataStatistic() {
        DataStatisticDialog dialog = DataStatisticDialog.newInstance();
        DataStatisticDialogPresenter dialogPresenter = new DataStatisticDialogPresenter(dialog);
        mGameBoxScoreView.popDataStatisticDialog(dialog);
    }

    @Override
    public void pressYourTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.YOUR_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressQuarter() {
        if (mTeamData.get(Constants.RecordDataType.QUARTER) == Integer.parseInt(mGameInfo.getTotalQuarter())){
            Log.d(TAG,"Quarter is already GG");
        }else{
            mTeamData.put(Constants.RecordDataType.QUARTER,mTeamData.get(Constants.RecordDataType.QUARTER)+1);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressUndo() {
        Log.d(TAG,"pressUndo executed");
        if (mUndoList.size() != 0) {
            undoDataInDb(0);
        }else {
            mGameBoxScoreView.showToast("undo history is empty !");
        }
    }

    @Override
    public void updateUi() {
        //裡面到時候可以放各部分updateUi
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void editDataInDb(int position, int type) {
        //TODO write data into DB
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.writeGameData(position,type);
        //TODO add to UNDOList
        mUndoList.addFirst(new Undo(type, mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER),mGameInfo.getStartingPlayerList().get(position)));
        String name = mGameInfo.getStartingPlayerList().get(position).getName();
        String number =mGameInfo.getStartingPlayerList().get(position).getNumber();
        Log.d(TAG,"number " + number +" " + name + " " + type +" + 1");
        updateUi();
        mUndoHistoryPresenter.notifyInsert();
    }

    @Override
    public LinkedList<Undo> getUndoList() {
        return mUndoList;
    }

    @Override
    public void undoDataInDb(int position) {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.undoGameData(position);
        mUndoList.remove(position);
        updateUi();
        mUndoHistoryPresenter.notifyRemove(position);
    }

}
