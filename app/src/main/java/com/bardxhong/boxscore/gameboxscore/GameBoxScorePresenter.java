package com.bardxhong.boxscore.gameboxscore;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;

import com.google.firebase.auth.FirebaseAuth;
import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.modelhelper.SharedPreferenceHelper;
import com.bardxhong.boxscore.modelhelper.GameInfoDbHelper;
import com.bardxhong.boxscore.modelhelper.firebasemodel.Create;
import com.bardxhong.boxscore.gameboxscore.changeplayer.ChangePlayerFragment;
import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.bardxhong.boxscore.gameboxscore.datarecord.DataRecordFragment;
import com.bardxhong.boxscore.gameboxscore.datarecord.DataRecordPresenter;
import com.bardxhong.boxscore.modelhelper.GameDataDbHelper;
import com.bardxhong.boxscore.gameboxscore.datastatistic.DataStatisticDialog;
import com.bardxhong.boxscore.gameboxscore.datastatistic.DataStatisticDialogPresenter;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.Player;
import com.bardxhong.boxscore.objects.Undo;
import com.bardxhong.boxscore.gameboxscore.changeplayer.ChangePlayerPresenter;
import com.bardxhong.boxscore.gameboxscore.undohistory.UndoHistoryFragment;
import com.bardxhong.boxscore.gameboxscore.undohistory.UndoHistoryPresenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameBoxScorePresenter implements GameBoxScoreContract.Presenter {

    private final String TAG = GameBoxScorePresenter.class.getSimpleName();

    private final GameBoxScoreContract.View mGameBoxScoreView;

    private android.support.v4.app.FragmentManager mFragmentManager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private DataRecordFragment mDataRecordFragment;
    private DataRecordPresenter mDataRecordPresenter;
    private ChangePlayerFragment mChangePlayerFragment;
    private ChangePlayerPresenter mChangePlayerPresenter;
    private UndoHistoryFragment mUndoHistoryFragment;
    private UndoHistoryPresenter mUndoHistoryPresenter;
    private List<Fragment> mFragmentList;

    private SparseIntArray mTeamData;
    private GameInfo mGameInfo;
    private LinkedList<Undo> mUndoList;


    public GameBoxScorePresenter(GameBoxScoreContract.View gameBoxScoreView, android.support.v4.app.FragmentManager manager) {

        mGameBoxScoreView = gameBoxScoreView;
        mFragmentManager = manager;
        mUndoList = new LinkedList<>();

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

    /**
     * Initial sparesarray of mTeamData and sharedpreferences
     */
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
        mUndoHistoryFragment = UndoHistoryFragment.newInstance();

        mChangePlayerPresenter = new ChangePlayerPresenter(mChangePlayerFragment, this);
        mDataRecordPresenter = new DataRecordPresenter(mDataRecordFragment, this);
        mUndoHistoryPresenter = new UndoHistoryPresenter(mUndoHistoryFragment, this);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mChangePlayerFragment);
        mFragmentList.add(mDataRecordFragment);
        mFragmentList.add(mUndoHistoryFragment);

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        mGameBoxScoreView.setViewPagerAdapter(mViewPagerFragmentAdapter);
    }

    @Override
    public void checkIsResume(boolean mIsResume) {

        if (mIsResume) {

            initResumeTeamData();
            mGameBoxScoreView.setGameInfoFromResume();
            mGameInfo.setGameId(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));

            // GameInfo
            initGameInfoFromDatabase();

            // PlayerList and 3D SparseArray
            setPlayerListFromDataBase();

        } else {
            // New game
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

    /**
     * When game is resumed, set PlayerList and DetailData from data in GameDataDb
     */
    private void setPlayerListFromDataBase() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);   //pass GameInfo to DB
        mGameDataDbHelper.setPlayerListFromDb();    //resume ArrayList<Player> in GameInfo
        mGameDataDbHelper.setDetailDataFromDb();    //resume 3D SparseArray
    }

    /**
     * When game is resumed, set mGameInfos from data in GameInfoDb
     */
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

    /**
     * When game is resumed, set mTeamData from sharedpreferences
     */
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

    /**
     * Initial data into database from mGameInfo when new game started
     */
    @Override
    public void writeInitDataIntoModel() {
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.setGameInfo(mGameInfo);
        mGameDataDbHelper.writeInitDetailDataIntoGameInfo();
        mGameDataDbHelper.writeInitDataIntoDataBase();

        GameInfoDbHelper mGameInfoDbHelper = BoxScore.getGameInfoDbHelper();
        mGameInfoDbHelper.setGameInfo(mGameInfo);
        mGameInfoDbHelper.writeGameInfoIntoDataBase();
    }

    /**
     * Clear sharedpreferences when user decides to discard not ended game
     */
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

    /**
     * Clear data in Db when user decides to discard not ended game
     */
    @Override
    public void removeGameDataInDataBase() {
        BoxScore.getGameInfoDbHelper().deleteGameInfo(null, SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
        BoxScore.getGameDataDbHelper().deleteGameData(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
    }

    @Override
    public void saveAndEndCurrentGame() {

        int newHistoryAmount = BoxScore.getGameInfoDbHelper().overPlayingGame(mGameInfo.getGameId(), mGameInfo.getYourTeamId());

        BoxScore.getTeamDbHelper().updateHistoryAmount(mGameInfo.getYourTeamId(), newHistoryAmount);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Create.getInstance().createGameData(BoxScore.getGameDataDbHelper().getSpecificGameData(mGameInfo.getGameId()));
            Create.getInstance().createGameInfo(BoxScore.getGameInfoDbHelper().getSpecificInfo(mGameInfo.getGameId()));
            Create.getInstance().updateTeamHistoryAmount(mGameInfo.getYourTeamId(), newHistoryAmount);
        }
        removeGameDataSharedPreferences();
    }

    /**
     * Called when user selects players for data record, then call function in two databases to update data and put information
     *
     * @param player for whom
     * @param type   for what
     */
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

    /**
     * Undo process in specific undo history.
     * Get information save in Undo object and pass to the reverse function in two DbHelpers
     *
     * @param position
     */
    @Override
    public void undoDataInDb(int position) {

        Undo undo = mUndoList.get(position);
        BoxScore.getGameDataDbHelper().minusData(undo.getPlayer(), undo.getType(), undo.getQuarter());
        BoxScore.getGameInfoDbHelper().writeGameData(undo.getType());

        mUndoList.remove(position);
        updateUi();
        mUndoHistoryPresenter.notifyRemove(position);
    }

    /**
     * Edit specific undo history for switching player, type, or both.
     * The process includes two parts : undo process and edit data process,
     * which will not do "remove" and "addFirst" to mUndoList, just set edited Undo object in its original position.
     *
     * @param position position in undo history recyclerview.
     */
    @Override
    public void editUndoHistoryAtPosition(int position, Player player, int type) {

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
    public void pressOpponentTeamScore() {
        Log.d(TAG, "Opponent Score +1");
        mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE) + 1);
        SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE));
        BoxScore.getGameInfoDbHelper().writeGameData(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE);
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void pressDataStatistic() {
        DataStatisticDialog dialog = DataStatisticDialog.newInstance(mGameInfo.getGameId());
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
    public void pressUndo() {
        Log.d(TAG, "pressUndo executed");

        if (mUndoList.size() != 0) {
            undoDataInDb(0);
        } else {
            mGameBoxScoreView.showToast("undo history is empty !");
        }
    }

    @Override
    public void pressMakeUp(Player player, int type, int quarter) {
        Log.d(TAG, "pressMakeUp executed");
        int result = BoxScore.getGameDataDbHelper().plusData(player, type, quarter);
        if (result > 0){
            mUndoList.addFirst(new Undo(type, quarter, player));

            updateUi();
            mUndoHistoryPresenter.notifyInsert();
        }
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
    public void longPressOpponentTeamFoul() {
        if (mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) > 0) {
            mTeamData.put(Constants.RecordDataType.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL) - 1);
            SharedPreferenceHelper.write(SharedPreferenceHelper.OPPONENT_TEAM_FOUL, mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL));
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
    public void scrollUp(int mPointerCount) {

        int value;

        switch (mPointerCount) {

            case 2:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.DOUBLE_UP, -1);
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
                Log.d(TAG, "double fingers scrolled UP.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_UP, -1);
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
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
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
                Log.d(TAG, "double fingers scrolled DOWN.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_DOWN, -1);
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
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
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
                Log.d(TAG, "double fingers scrolled LEFT.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_LEFT, -1);
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
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
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
                Log.d(TAG, "double fingers scrolled RIGHT.");
                break;

            case 3:
                value = SharedPreferenceHelper.read(SharedPreferenceHelper.TRIPLE_RIGHT, -1);
                if (value != -1) mDataRecordPresenter.popPlayerSelectProcess(value);
                Log.d(TAG, "triple fingers scrolled RIGHT.");
                break;
        }
    }

    @Override
    public void updateUi() {
        //裡面到時候可以放各部分updateUi
        mGameBoxScoreView.updateUiTeamData();
    }

    @Override
    public void start() {
        mGameBoxScoreView.setInitDataOnScreen(mTeamData);
    }

}
