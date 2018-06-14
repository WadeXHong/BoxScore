package com.wadexhong.boxscore.modelhelper.task;

import android.database.Cursor;
import android.os.Environment;

import com.wadexhong.boxscore.BoxScore;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by wade8 on 2018/6/14.
 */
public class CreateExcelTaskTest {


    public CreateExcelTaskTest(){
    }

    @Test
    public void getSpecificData(){
        String mockGameId = "ad655506-7ad5-43dd-90f1-32c1c04a2b3b";
        Cursor cursorGameData = BoxScore.getGameDataDbHelper().getSpecificGameData(mockGameId);
        Cursor cursorGameInfo = BoxScore.getGameInfoDbHelper().getSpecificInfo(mockGameId);

        assertTrue(cursorGameData.moveToFirst());
        assertTrue(cursorGameInfo.moveToFirst());
    }

    @Test
    public void doInBackground() throws Exception {

        File mock = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BoxScore/");
        File mockXls = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BoxScore/ad655506-7ad5-43dd-90f1-32c1c04a2b3b.xls");

        assertTrue(mock.exists());
        assertTrue(mockXls.exists());
    }

    @Test
    public void onPostExecute() throws Exception {
    }

}