package com.wadexhong.boxscore;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Create;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Get;
import com.wadexhong.boxscore.modelhelper.SharedPreferenceHelper;

/**
 * Created by wade8 on 2018/5/1.
 */

public class BoxScorePresenter implements BoxScoreContract.Presenter {

    private final String TAG = BoxScorePresenter.class.getSimpleName();

    private final BoxScoreContract.View mBoxScoreView;

    public BoxScorePresenter(BoxScoreContract.View boxScoreView) {
        mBoxScoreView = boxScoreView;
        mBoxScoreView.setPresenter(this);
    }

    @Override
    public void start() {
        updateDbFromFireBase();
    }

    @Override
    public void transToStartGame() {
        mBoxScoreView.transToStartGame();
    }

    @Override
    public void transToTeamManage() {

    }

    @Override
    public void transToGameHistory() {

    }

    @Override
    public void transToSetting() {

    }

    @Override
    public void pressStartGame() {

        if (SharedPreferenceHelper.contains(SharedPreferenceHelper.PLAYING_GAME)) {
            // 由 SharedPreferences 拿到的 gameId 對 DataBase 做 query.
            Cursor cursor = BoxScore.getGameInfoDbHelper()
                      .getReadableDatabase()
                      .query(Constants.GameInfoDBContract.TABLE_NAME
                                , null
                                , Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " = ?"
                                , new String[]{SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "")}, null, null, null);

            cursor.moveToFirst();

            try {
                // 若拿的到名稱代表資料存在，將內容傳至View顯示提示訊息。
                mBoxScoreView.askResumeGame(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)));

            } catch (CursorIndexOutOfBoundsException e) {
                // 拿不到代表資料損毀或 SharedPreferences 未順利清除，則清除後直接開新遊戲。
                Log.w(TAG, e.getMessage());
                removeGameDataSharedPreferences();

                Log.w(TAG, "SharedPreferences have been removed in order to making function executed normally");
                transToStartGame();

            }
            cursor.close();

        } else {
            transToStartGame();
        }
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
        Log.d(TAG, "removeGameDataSharedPreferences executed");
    }

    @Override
    public void removeGameDataInDataBase() {
        BoxScore.getGameInfoDbHelper().deleteGameInfo(null, SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
        BoxScore.getGameDataDbHelper().deleteGameData(SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, ""));
    }

    @Override
    public void updateDbFromFireBase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mBoxScoreView.showProgressBarDialog(BoxScore.getAppContext().getString(R.string.progressbar_loading));
            String notEndedGameId = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "");
            // 改用檢查取代每次都清空
//            BoxScore.getGameDataDbHelper().deleteAll(notEndedGameId);
//            BoxScore.getGameInfoDbHelper().deleteAll(notEndedGameId);
//            BoxScore.getTeamDbHelper().deleteAll();
            Get.getInstance().onCreate();
        }
    }

    @Override
    public void saveAndEndCurrentGame() {

        String gameId = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "");
        String teamId = SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_ID, "");

        if (!gameId.equals("") && !teamId.equals("")) {

            int newHistoryAmount = BoxScore.getGameInfoDbHelper().overPlayingGame(gameId, teamId);

            BoxScore.getTeamDbHelper().updateHistoryAmount(teamId, newHistoryAmount);

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Create.getInstance().createGameData(BoxScore.getGameDataDbHelper().getSpecificGameData(gameId));
                Create.getInstance().createGameInfo(BoxScore.getGameInfoDbHelper().getSpecificInfo(gameId));
                Create.getInstance().updateTeamHistoryAmount(teamId, newHistoryAmount);
            }
        }
        removeGameDataSharedPreferences();
    }
}
