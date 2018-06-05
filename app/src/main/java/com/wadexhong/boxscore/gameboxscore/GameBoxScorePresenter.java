package com.wadexhong.boxscore.gameboxscore;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;

import com.google.firebase.auth.FirebaseAuth;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.modelhelper.SharedPreferenceHelper;
import com.wadexhong.boxscore.modelhelper.GameInfoDbHelper;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Create;
import com.wadexhong.boxscore.gameboxscore.changeplayer.ChangePlayerFragment;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.gameboxscore.datarecord.DataRecordFragment;
import com.wadexhong.boxscore.gameboxscore.datarecord.DataRecordPresenter;
import com.wadexhong.boxscore.modelhelper.GameDataDbHelper;
import com.wadexhong.boxscore.gameboxscore.datastatistic.DataStatisticDialog;
import com.wadexhong.boxscore.gameboxscore.datastatistic.DataStatisticDialogPresenter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.Undo;
import com.wadexhong.boxscore.gameboxscore.changeplayer.ChangePlayerPresenter;
import com.wadexhong.boxscore.gameboxscore.undohistory.UndoHistoryFragment;
import com.wadexhong.boxscore.gameboxscore.undohistory.UndoHistoryPresenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameBoxScorePresenter implements GameBoxScoreContract.Presenter {

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


    public GameBoxScorePresenter(GameBoxScoreContract.View gameBoxScoreView, android.support.v4.app.FragmentManager manager) {

        mGameBoxScoreView = gameBoxScoreView;
        mFragmentManager = manager;
        mUndoList = new LinkedList<Undo>();

        gameBoxScoreView.setPresenter(this);
        setViewPager();
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public LinkedList<Undo> getUndoList() {
        return mUndoList;
    }


    private void initNewTeamData() {

        mTeamData = new SparseIntArray();
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, 0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, 0);
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL, 0);
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL, 0);
        mTeamData.append(Constants.RecordDataType.QUARTER, 1);

        //TODO 或許可以捨去整個TeamData SparseArray 全部改用SharedPreferences
        SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL, 0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, 0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, 0);
        SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER, 1);

    }


    private void setViewPager() {

        mChangePlayerFragment = ChangePlayerFragment.newInstance();
        mDataRecordFragment = DataRecordFragment.newInstance();
//        mDataStatisticFragment = DataStatisticFragment.newInstance();
        mUndoHistoryFragment = UndoHistoryFragment.newInstance();

        mChangePlayerPresenter = new ChangePlayerPresenter(mChangePlayerFragment, this);
        mDataRecordPresenter = new DataRecordPresenter(mDataRecordFragment, this);
//        mDataStatisticPresenter = new DataStatisticPresenter(mDataStatisticFragment);
        mUndoHistoryPresenter = new UndoHistoryPresenter(mUndoHistoryFragment, this);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mChangePlayerFragment);
        mFragmentList.add(mDataRecordFragment);
//        mFragmentList.add(mDataStatisticFragment);
        mFragmentList.add(mUndoHistoryFragment);

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        mGameBoxScoreView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }

    @Override
    public void start() {
        mGameBoxScoreView.setInitDataOnScreen(mTeamData);
    }

    @Override
    public void checkIsResume(boolean mIsResume) {

        if (mIsResume) {

            initResumeTeamData();
            mGameBoxScoreView.setGameInfoFromResume();
            mGameInfo.setGameId(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));

            //GameInfo
            initGameInfoFromDatabase();

            //PlayerList and 3D SparseArray
            setPlayerListFromDataBase();

        } else {
            initNewTeamData();
            mGameBoxScoreView.setGameInfoFromInput();
            mGameInfo = mGameBoxScoreView.getGameInfo();
            mGameInfo.setGameId(UUID.randomUUID().toString());
            SharedPreferenceHelper.write(SharedPreferenceHelper.PLAYING_GAME, mGameInfo.getGameId());
            SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_ID, mGameInfo.getYourTeamId());
            writeInitDataIntoModel();
        }
        mGameInfo.setTeamData(mTeamData);
    }


    private void setPlayerListFromDataBase() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);   //pass GameInfo to DB
        mGameDataDbHelper.setPlayerListFromDb();    //resume ArrayList<Player> in GameInfo
        mGameDataDbHelper.setDetailDataFromDb();    //resume 3D SparseArray

//        GameInfoDbHelper mGameInfoDbHelper = BoxScore.getGameInfoDbHelper();
//        mGameInfoDbHelper.setGameInfo(mGameInfo);
//        mGameInfoDbHelper.writeGameInfoIntoDataBase();

    }

    private void initGameInfoFromDatabase() {

        BoxScore.getGameInfoDbHelper().setGameInfo(mGameInfo);

        Cursor cursor = BoxScore.getGameInfoDbHelper().getGameInfo();
        cursor.moveToFirst();

        mGameInfo.setTimeoutFirstHalf(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF)));
        mGameInfo.setTimeoutSecondHalf(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF)));
        mGameInfo.setMaxFoul(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL)));
        mGameInfo.setTotalQuarter(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER)));
        mGameInfo.setQuarterLength(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH)));
        mGameInfo.setYourTeamId(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID)));
        mGameInfo.setYourTeam(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM)));
        mGameInfo.setOpponentName(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)));
        mGameInfo.setGameName(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME)));
        mGameInfo.setGameDate(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE)));

        cursor.close();
    }

    private void initResumeTeamData() {

        mTeamData = new SparseIntArray();

        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE, SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE, 0));
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, SharedPreferenceHelper.read(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, 0));
        mTeamData.append(Constants.RecordDataType.YOUR_TEAM_FOUL, SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_FOUL, 0));
        mTeamData.append(Constants.RecordDataType.OPPONENT_TEAM_FOUL, SharedPreferenceHelper.read(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, 0));
        mTeamData.append(Constants.RecordDataType.QUARTER, SharedPreferenceHelper.read(SharedPreferenceHelper.QUARTER, 1));

    }

    @Override
    public GameInfo resumeGameInfo(GameInfo gameInfo) {
        mGameInfo = gameInfo;
        return mGameInfo;
    }


    @Override
    public void removeGameDataSharedPreferences() {
        SharedPreferenceHelper.remove(SharedPreferenceHelper.PLAYING_GAME);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_ID);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_FOUL);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_FOUL);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE);
        SharedPreferenceHelper.remove(SharedPreferenceHelper.QUARTER);
    }

    @Override
    public void removeGameDataInDataBase() {
        BoxScore.getGameDataDbHelper().removeGameData(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
        BoxScore.getGameInfoDbHelper().removeGameInfo(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
    }

    @Override
    public void writeInitDataIntoModel() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);
        mGameDataDbHelper.writeInitDataIntoGameInfo();
        mGameDataDbHelper.writeInitDataIntoDataBase();

        GameInfoDbHelper mGameInfoDbHelper = BoxScore.getGameInfoDbHelper();
        mGameInfoDbHelper.setGameInfo(mGameInfo);
        mGameInfoDbHelper.writeGameInfoIntoDataBase();
    }


    @Override
    public void pressOpponentTeamScore() {
        Log.d(TAG, "Opponent Score +1");
        mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE) + 1);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
        BoxScore.getGameInfoDbHelper().writeGameData(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE);
        mGameBoxScoreView.updateUiTeamData();
    }

    //TODO deprecate
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
        if (mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())) {
            Log.d(TAG, "TeamFoul is GG");
        } else {
            mTeamData.put(Constants.RecordDataType.YOUR_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL) + 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) == Integer.parseInt(mGameInfo.getMaxFoul())) {
            Log.d(TAG, "TeamFoul is GG");
        } else {
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) + 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void longPressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) > 0) {
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) - 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void pressQuarter() {
        if (mTeamData.get(Constants.RecordDataType.QUARTER) == Integer.parseInt(mGameInfo.getTotalQuarter())) {
            Log.d(TAG, "Quarter is already Max");
        } else {
            //TODO TIMEOUTS
            mTeamData.put(Constants.RecordDataType.QUARTER, mTeamData.get(Constants.RecordDataType.QUARTER) + 1);
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL, 0);
            mTeamData.put(Constants.RecordDataType.YOUR_TEAM_FOUL, 0);
            SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER, mTeamData.get(Constants.RecordDataType.QUARTER));
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
            SharedPreferenceHelper.write(SharedPreferenceHelper.YOUR_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL));

            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void longPressQuarter() {
        if (mTeamData.get(Constants.RecordDataType.QUARTER) > 0) {
            mTeamData.put(Constants.RecordDataType.QUARTER, mTeamData.get(Constants.RecordDataType.QUARTER) - 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.QUARTER, mTeamData.get(Constants.RecordDataType.QUARTER));
            mGameBoxScoreView.updateUiTeamData();
        }
    }

    @Override
    public void saveAndEndCurrentGame() {

        int newHistoryAmount = BoxScore.getGameInfoDbHelper().overExpandingGame(mGameInfo.getGameId(), mGameInfo.getYourTeamId());

        BoxScore.getTeamDbHelper().updateHistoryAmount(mGameInfo.getYourTeamId(), newHistoryAmount);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Create.getInstance().CreateGameData(BoxScore.getGameDataDbHelper().getSpecificGameData(mGameInfo.getGameId()));
            Create.getInstance().CreateGameInfo(BoxScore.getGameInfoDbHelper().getSpecificInfo(mGameInfo.getGameId()));
            Create.getInstance().UpdateTeamHistoryAmount(mGameInfo.getYourTeamId(), newHistoryAmount);
        }
        removeGameDataSharedPreferences();
    }

    @Override
    public void pressUndo() {
        Log.d(TAG, "pressUndo executed");
        if (mUndoList.size() != 0) {
            undoDataInDb(0);
        } else {
            mGameBoxScoreView.showToast("undo history is empty !");
        }
    }

    @Override
    public void updateUi() {
        //裡面到時候可以放各部分updateUi
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void editDataInDb(Player player, int type) {
        //write data into DB
        BoxScore.getGameDataDbHelper().plusData(player, type, 0);
        BoxScore.getGameInfoDbHelper().writeGameData(type);
        //add to UNDOList
        mUndoList.addFirst(new Undo(type, mGameInfo.getTeamData().get(Constants.RecordDataType.QUARTER), player));

        updateUi();
        mUndoHistoryPresenter.notifyInsert();

    }


    @Override
    public void undoDataInDb(int position) {
        Undo undo = mUndoList.get(position);
        BoxScore.getGameDataDbHelper().minusData(undo.getPlayer(), undo.getType(), undo.getQuarter());
        BoxScore.getGameInfoDbHelper().writeGameData(undo.getType());

        mUndoList.remove(position);
        updateUi();
        mUndoHistoryPresenter.notifyRemove(position);
    }


    @Override
    public void editAtPosition(int position, Player player, int type) {
        Undo undo = mUndoList.get(position);
        BoxScore.getGameDataDbHelper().minusData(undo.getPlayer(), undo.getType(), undo.getQuarter());
        BoxScore.getGameInfoDbHelper().writeGameData(undo.getType());

        BoxScore.getGameDataDbHelper().plusData(player, type, undo.getQuarter());
        BoxScore.getGameInfoDbHelper().writeGameData(type);

        undo.setMarked(false);
        undo.setPlayer(player);
        undo.setType(type);
        mUndoList.set(position, undo);

        updateUi();
        mUndoHistoryPresenter.notifyEdit(position);

    }

    @Override
    public void scrollUp(int mPointerCount) {

        int value;

        switch (mPointerCount) {

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_UP, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "double fingers scrolled UP.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_UP, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "triple fingers scrolled UP.");
                break;
        }
    }

    @Override
    public void scrollDown(int mPointerCount) {

        int value;

        switch (mPointerCount) {

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_DOWN, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "double fingers scrolled DOWN.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_DOWN, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "triple fingers scrolled DOWN.");
                break;
        }
    }

    @Override
    public void scrollLeft(int mPointerCount) {

        int value;

        switch (mPointerCount) {

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_LEFT, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "double fingers scrolled LEFT.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_LEFT, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "triple fingers scrolled LEFT.");
                break;
        }
    }

    @Override
    public void scrollRight(int mPointerCount) {

        int value;

        switch (mPointerCount) {

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_RIGHT, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "double fingers scrolled RIGHT.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_RIGHT, -1);
                if (value != -1) {
                    mDataRecordPresenter.popPlayerSelectProcess(value);
                }
                Log.d(TAG, "triple fingers scrolled RIGHT.");
                break;
        }
    }

}
