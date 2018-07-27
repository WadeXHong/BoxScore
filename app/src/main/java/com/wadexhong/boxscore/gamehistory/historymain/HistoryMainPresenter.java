package com.wadexhong.boxscore.gamehistory.historymain;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.dialog.ProgressBarDialog;
import com.wadexhong.boxscore.gamehistory.GameHistoryContract;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Create;
import com.wadexhong.boxscore.modelhelper.firebasemodel.Delete;
import com.wadexhong.boxscore.modelhelper.firebasemodel.UploadExcelFileCallBack;
import com.wadexhong.boxscore.modelhelper.task.CreateExcelCallBack;
import com.wadexhong.boxscore.modelhelper.task.CreateExcelTask;


/**
 * Created by wade8 on 2018/5/22.
 */

public class HistoryMainPresenter implements HistoryMainContract.Presenter {

    private static final String TAG = HistoryMainPresenter.class.getSimpleName();

    private final HistoryMainContract.View mHistoryMainView;
    private GameHistoryContract.Presenter mGameHistoryPresenter;

    public HistoryMainPresenter(HistoryMainContract.View historyMainView, GameHistoryContract.Presenter gameHistoryPresenter) {
        mHistoryMainView = historyMainView;
        mGameHistoryPresenter = gameHistoryPresenter;

        mHistoryMainView.setPresenter(this);
    }

    @Override
    public Cursor getGameHistory() {
        return BoxScore.getGameInfoDbHelper().getGameHistory();
    }

    @Override
    public void setGameHistoryToolBar() {
        mGameHistoryPresenter.setGameHistoryToolBar();
    }

    @Override
    public void transToDetail(String gameId) {
        mGameHistoryPresenter.transToDetail(gameId);
    }

    @Override
    public void confirmShareGameHistory(String gameId) {
        mHistoryMainView.confirmShareGameHistory(gameId);
    }

    @Override
    public void deleteGameHistory(String teamId, String gameId) {
        int count = BoxScore.getGameInfoDbHelper().deleteGameInfo(teamId, gameId);
        if (count != -1) { // if -1 means there is no game in team can be delete
            BoxScore.getTeamDbHelper().updateHistoryAmount(teamId, count);
            BoxScore.getGameDataDbHelper().deleteGameData(gameId);
            Delete.getInstance().deleteGame(teamId, gameId, count);
        }
    }

    @Override
    public void createAndShareGameHistoryXls(final String gameId) {

        new CreateExcelTask(gameId, new CreateExcelCallBack() {
            @Override
            public void onSuccess(Uri uri) {
                //FireBase Storage
                Create.getInstance().uploadExcelFile(gameId, uri, new UploadExcelFileCallBack() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "createAndShareGameHistoryXls success : " + uri.toString());
                        mHistoryMainView.saveUrlInClipboard(uri.toString());
                        ProgressBarDialog.hideProgressBarDialog();
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d(TAG, "createAndShareGameHistoryXls fail : " + message);
                        ProgressBarDialog.hideProgressBarDialog();
                    }
                });
            }

            @Override
            public void onError() {
                mHistoryMainView.showToast(BoxScore.getStringEasy(R.string.toast_cannot_create_excel));
                ProgressBarDialog.hideProgressBarDialog();
            }
        }).execute();
    }

    @Override
    public void start() {

    }
}
