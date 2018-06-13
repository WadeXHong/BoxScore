package com.wadexhong.boxscore.gamehistory.historymain;

import android.database.Cursor;
import android.os.Environment;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gamehistory.GameHistoryContract;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.modelhelper.task.CreateExcelCallBack;
import com.wadexhong.boxscore.modelhelper.task.CreateExcelTask;

import java.io.File;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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
    public void createAndShareGameHistoryXls(String gameId) {

        new CreateExcelTask(gameId, new CreateExcelCallBack() {
            @Override
            public void onComplete() {
                //FireBase Storage
            }

            @Override
            public void onError() {
                mHistoryMainView.showToast(BoxScore.getStringEasy(R.string.toast_cannot_create_excel));
            }
        }).execute();
    }

    @Override
    public void start() {

    }
}
