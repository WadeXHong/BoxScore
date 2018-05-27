package com.wadexhong.boxscore.gameboxscore;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.SharedPreferenceHelper;
import com.wadexhong.boxscore.dbhelper.GameInfoDbHelper;
import com.wadexhong.boxscore.playeroncourt.ChangePlayerFragment;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.datarecord.DataRecordFragment;
import com.wadexhong.boxscore.datarecord.DataRecordPresenter;
import com.wadexhong.boxscore.dbhelper.GameDataDbHelper;
import com.wadexhong.boxscore.dialogfragment.datastatistic.DataStatisticDialog;
import com.wadexhong.boxscore.dialogfragment.datastatistic.DataStatisticDialogPresenter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Undo;
import com.wadexhong.boxscore.playeroncourt.ChangePlayerPresenter;
import com.wadexhong.boxscore.undohistory.UndoHistoryFragment;
import com.wadexhong.boxscore.undohistory.UndoHistoryPresenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
        setViewPager();
        mUndoList = new LinkedList<Undo>();
    }

    private void initNewTeamData() {
        mTeamData = new SparseIntArray();
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,0);
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL,0);
        mTeamData.append(Constants.RecordDataType.QUARTER,1);

        //TODO 或許可以捨去整個TeamData SparseArray 全部改用SharedPreferences
        SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL,0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL,0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE,0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE,0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER,1);

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
        mFragmentList.add(mChangePlayerFragment);
        mFragmentList.add(mDataRecordFragment);
//        mFragmentList.add(mDataStatisticFragment);
        mFragmentList.add(mUndoHistoryFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        mGameBoxScoreView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }


    @Override
    public void start() {
        mGameBoxScoreView.setInitDataOnScreen(mTeamData);
    }


    @Override
    public void checkIsResume(boolean mIsResume) {
        if (mIsResume){
            initResumeTeamData();
            mGameBoxScoreView.setGameInfoFromResume();
            mGameInfo.setGameId(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,""));
            BoxScore.getGameInfoDbHelper().setGameInfo(mGameInfo);
            initGameInfoFromDatabase();
            //TODO call model
            //1. PlayerList
            setPlayerListFromDataBase();
            //2. Detail 3-D SparseArray



        }else {
            initNewTeamData();
            mGameBoxScoreView.setGameInfoFromInput();
            mGameInfo = mGameBoxScoreView.getGameInfo();
            mGameInfo.setGameId(UUID.randomUUID().toString());
            SharedPreferenceHelper.write(SharedPreferenceHelper.PLAYING_GAME,mGameInfo.getGameId());
            writeInitDataIntoModel();
        }
        mGameInfo.setTeamData(mTeamData);
    }

    private void setPlayerListFromDataBase() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);
        mGameDataDbHelper.setUndoList(mUndoList);
        mGameDataDbHelper.setPlayerListFromDb();
        mGameDataDbHelper.setDetailDataFromDb();

//        GameInfoDbHelper mGameInfoDbHelper = BoxScore.getGameInfoDbHelper();
//        mGameInfoDbHelper.setGameInfo(mGameInfo);
//        mGameInfoDbHelper.writeGameInfoIntoDataBase();

    }

    private void initGameInfoFromDatabase() {
        Cursor cursor = BoxScore.getGameInfoDbHelper().getGameInfo();
        cursor.moveToFirst();
        mGameInfo.setTimeoutFirstHalf(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.TIMEOUT_FIRST_HALF)));
        mGameInfo.setTimeoutSecondHalf(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.TIMEOUT_SECOND_HALF)));
        mGameInfo.setMaxFoul(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.MAX_FOUL)));
        mGameInfo.setTotalQuarter(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.TOTAL_QUARTER)));
        mGameInfo.setQuarterLength(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.QUARTER_LENGTH)));
        mGameInfo.setYourTeam(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM)));
        mGameInfo.setOpponentName(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_NAME)));
        mGameInfo.setGameName(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.GAME_NAME)));
        mGameInfo.setGameDate(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.GAME_DATE)));
        cursor.close();
    }


    private void initResumeTeamData() {
        mTeamData = new SparseIntArray();
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0));
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,SharedPreferenceHelper.read(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, 0));
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL,SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_FOUL, 0));
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL,SharedPreferenceHelper.read(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, 0));
        mTeamData.append(Constants.RecordDataType.QUARTER,SharedPreferenceHelper.read(SharedPreferenceHelper.QUARTER, 1));

    }

    @Override
    public GameInfo resumeGameInfo(GameInfo gameInfo) {
        mGameInfo = gameInfo;
        return mGameInfo;
    }

    @Override
    public void removeGameDataSharedPreferences() {
        SharedPreferenceHelper.remove(SharedPreferenceHelper.PLAYING_GAME);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_FOUL);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_FOUL);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.QUARTER);
    }

    @Override
    public void removeGameDataInDataBase() {
        BoxScore.getGameDataDbHelper().removeGameData(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,""));
        BoxScore.getGameInfoDbHelper().removeGameInfo(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,""));
    }




    @Override
    public void writeInitDataIntoModel() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);
        mGameDataDbHelper.setUndoList(mUndoList);
        mGameDataDbHelper.writeInitDataIntoGameInfo();
        mGameDataDbHelper.writeInitDataIntoDataBase();

        GameInfoDbHelper mGameInfoDbHelper = BoxScore.getGameInfoDbHelper();
        mGameInfoDbHelper.setGameInfo(mGameInfo);
        mGameInfoDbHelper.writeGameInfoIntoDataBase();
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void pressOpponentTeamScore() {
        Log.d(TAG,"Opponent Score +1");
        mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE) + 1);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
        BoxScore.getGameInfoDbHelper().writeGameData(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE);
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void longPressOpponentTeamScore() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE) > 0) {
            Log.d(TAG, "Opponent Score -1");
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE) - 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
            BoxScore.getGameInfoDbHelper().writeGameData(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE);
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressDataStatistic() {
        DataStatisticDialog dialog = DataStatisticDialog.newInstance();
        DataStatisticDialogPresenter dialogPresenter = new DataStatisticDialogPresenter(dialog);
        mGameBoxScoreView.popDataStatisticDialog(dialog);
    }

    @Override
    public void pressYourTeamFoul() { //TODO will be deprecated
        if (mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.YOUR_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL)+1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())){
            Log.d(TAG,"TeamFoul is GG");
        }else{
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL)+1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void longPressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) > 0){
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL)-1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL,mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressQuarter() {
        if (mTeamData.get(Constants.RecordDataType.QUARTER) == Integer.parseInt(mGameInfo.getTotalQuarter())){
            Log.d(TAG,"Quarter is already GG");
        }else{
            mTeamData.put(Constants.RecordDataType.QUARTER,mTeamData.get(Constants.RecordDataType.QUARTER)+1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER,mTeamData.get(Constants.RecordDataType.QUARTER));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void longPressQuarter() {
        if (mTeamData.get(Constants.RecordDataType.QUARTER) > 0){
            mTeamData.put(Constants.RecordDataType.QUARTER,mTeamData.get(Constants.RecordDataType.QUARTER)-1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER,mTeamData.get(Constants.RecordDataType.QUARTER));
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

        BoxScore.getGameInfoDbHelper().writeGameData(type);
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

    @Override
    public void scrollUp(int mPointerCount) {

        int value = -1;

        switch (mPointerCount){
//            case 1:
////                mDataRecordPresenter.pressFreeThrowMade();
//                Log.d(TAG,"single finger scrolled UP.");
//                break;
            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_UP,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressTwoPointMade();
                Log.d(TAG,"double fingers scrolled UP.");
                break;
            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_UP,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressThreePointMade();
                Log.d(TAG,"triple fingers scrolled UP.");
                break;
        }
    }

    @Override
    public void scrollDown(int mPointerCount) {

        int value = -1;

        switch (mPointerCount){
//            case 1:
//                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_DOWN,-1);
//                if (value != -1){
//                    mDataRecordPresenter.popPlayerSelectProccess(value);
//                }
////                mDataRecordPresenter.pressFreeThrowMissed();
//                Log.d(TAG,"single finger scrolled DOWN.");
//                break;
            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_DOWN,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressTwoPointMissed();
                Log.d(TAG,"double fingers scrolled DOWN.");
                break;
            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_DOWN,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressThreePointMissed();
                Log.d(TAG,"triple fingers scrolled DOWN.");
                break;
        }
    }

    @Override
    public void scrollLeft(int mPointerCount) {

        int value = -1 ;

        switch (mPointerCount){

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_LEFT,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressDefensiveRebound();
                Log.d(TAG,"double fingers scrolled LEFT.");
                break;
            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_LEFT,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressBlock();
                Log.d(TAG,"triple fingers scrolled LEFT.");
                break;
        }
    }

    @Override
    public void scrollRight(int mPointerCount) {

        int value = -1 ;

        switch (mPointerCount){

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_RIGHT,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressOffensiveRebound();
                Log.d(TAG,"double fingers scrolled RIGHT.");
                break;
            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_RIGHT,-1);
                if (value != -1){
                    mDataRecordPresenter.popPlayerSelectProccess(value);
                }
//                mDataRecordPresenter.pressSteal();
                Log.d(TAG,"triple fingers scrolled RIGHT.");
                break;
        }
    }

}
