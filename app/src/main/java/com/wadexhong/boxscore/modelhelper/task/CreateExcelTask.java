package com.wadexhong.boxscore.modelhelper.task;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by wade8 on 2018/6/13.
 */

        public class CreateExcelTask extends AsyncTask<Void, Void, Void> {

            private boolean mIsTaskSuccess = false;
            private String mGameId;
            private CreateExcelCallBack mCreateExcelCallBack;
            private Uri mUri;

            public CreateExcelTask(String gameId, CreateExcelCallBack createExcelCallBack) {
                super();

        mGameId = gameId;
        mCreateExcelCallBack = createExcelCallBack;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String fileName = mGameId + ".xls";

        try {
            Cursor cursorGameData = BoxScore.getGameDataDbHelper().getSpecificGameData(mGameId);
            Cursor cursorGameInfo = BoxScore.getGameInfoDbHelper().getSpecificInfo(mGameId);

            if (cursorGameData.moveToFirst() && cursorGameInfo.moveToFirst()) {

                File directory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BoxScore/");
                if (!directory.isDirectory()) directory.mkdirs();

                File file = new File(directory, fileName);
                mUri = Uri.fromFile(file);
                WorkbookSettings workbookSettings = new WorkbookSettings();
                workbookSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings);
                WritableSheet sheetGameData = workbook.createSheet("GameData", 0);
                WritableSheet sheetGameInfo = workbook.createSheet("GameInfo", 1);

                sheetGameData.addCell(new Label(0, 0, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER));
                sheetGameData.addCell(new Label(1, 0, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME));
                sheetGameData.addCell(new Label(2, 0, Constants.GameDataDBContract.COLUMN_NAME_QUARTER));
                sheetGameData.addCell(new Label(3, 0, Constants.GameDataDBContract.COLUMN_NAME_POINTS));
                sheetGameData.addCell(new Label(4, 0, Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE));
                sheetGameData.addCell(new Label(5, 0, Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED));
                sheetGameData.addCell(new Label(6, 0, Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE));
                sheetGameData.addCell(new Label(7, 0, Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED));
                sheetGameData.addCell(new Label(8, 0, Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE));
                sheetGameData.addCell(new Label(9, 0, Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED));
                sheetGameData.addCell(new Label(10, 0, Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND));
                sheetGameData.addCell(new Label(11, 0, Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND));
                sheetGameData.addCell(new Label(12, 0, Constants.GameDataDBContract.COLUMN_NAME_ASSIST));
                sheetGameData.addCell(new Label(13, 0, Constants.GameDataDBContract.COLUMN_NAME_STEAL));
                sheetGameData.addCell(new Label(14, 0, Constants.GameDataDBContract.COLUMN_NAME_BLOCK));
                sheetGameData.addCell(new Label(15, 0, Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL));
                sheetGameData.addCell(new Label(16, 0, Constants.GameDataDBContract.COLUMN_NAME_TURNOVER));

                sheetGameInfo.addCell(new Label(0, 0, Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM));
                sheetGameInfo.addCell(new Label(1, 0, Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME));
                sheetGameInfo.addCell(new Label(2, 0, Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE));
                sheetGameInfo.addCell(new Label(3, 0, Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE));
                sheetGameInfo.addCell(new Label(4, 0, Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE));


                int rowGameData = 1;
                do {
                    sheetGameData.addCell(new Label(0, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER))));
                    sheetGameData.addCell(new Label(1, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME))));
                    sheetGameData.addCell(new Label(2, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_QUARTER))));
                    sheetGameData.addCell(new Label(3, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_POINTS))));
                    sheetGameData.addCell(new Label(4, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE))));
                    sheetGameData.addCell(new Label(5, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED))));
                    sheetGameData.addCell(new Label(6, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE))));
                    sheetGameData.addCell(new Label(7, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED))));
                    sheetGameData.addCell(new Label(8, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE))));
                    sheetGameData.addCell(new Label(9, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED))));
                    sheetGameData.addCell(new Label(10, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND))));
                    sheetGameData.addCell(new Label(11, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND))));
                    sheetGameData.addCell(new Label(12, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_ASSIST))));
                    sheetGameData.addCell(new Label(13, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_STEAL))));
                    sheetGameData.addCell(new Label(14, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_BLOCK))));
                    sheetGameData.addCell(new Label(15, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL))));
                    sheetGameData.addCell(new Label(16, rowGameData, cursorGameData.getString(cursorGameData.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER))));
                    rowGameData++;
                } while (cursorGameData.moveToPosition(rowGameData));

                sheetGameInfo.addCell(new Label(0, 1, cursorGameInfo.getString(cursorGameInfo.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM))));
                sheetGameInfo.addCell(new Label(1, 1, cursorGameInfo.getString(cursorGameInfo.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME))));
                sheetGameInfo.addCell(new Label(2, 1, cursorGameInfo.getString(cursorGameInfo.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE))));
                sheetGameInfo.addCell(new Label(3, 1, cursorGameInfo.getString(cursorGameInfo.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE))));
                sheetGameInfo.addCell(new Label(4, 1, cursorGameInfo.getString(cursorGameInfo.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE))));

                workbook.write();
                workbook.close();
                mIsTaskSuccess = true;
            } else {
                mIsTaskSuccess = false;
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
            mIsTaskSuccess = false;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mIsTaskSuccess) {
            mCreateExcelCallBack.onSuccess(mUri);
        } else {
            mCreateExcelCallBack.onError();
        }
    }
}
