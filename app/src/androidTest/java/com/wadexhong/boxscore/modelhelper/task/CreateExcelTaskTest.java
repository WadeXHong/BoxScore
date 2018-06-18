package com.wadexhong.boxscore.modelhelper.task;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.wadexhong.boxscore.BoxScore;

import org.junit.Test;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static org.junit.Assert.*;

/**
 * Created by wade8 on 2018/6/14.
 */
public class CreateExcelTaskTest {

    private final String mockGameId = "ad655506-7ad5-43dd-90f1-32c1c04a2b3b";

    public CreateExcelTaskTest(){
    }

    @Test
    public void getSpecificData(){
        Cursor cursorGameData = BoxScore.getGameDataDbHelper().getSpecificGameData(mockGameId);
        Cursor cursorGameInfo = BoxScore.getGameInfoDbHelper().getSpecificInfo(mockGameId);

        assertTrue(cursorGameData.moveToFirst());
        assertTrue(cursorGameInfo.moveToFirst());
    }

    @Test
    public void workBookSettingTest(){

    }

    @Test
    public void doInBackground() throws Exception {

        String mockRandomFileName = UUID.randomUUID().toString() + ".xls";

        File mockDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BoxScore/");
        if (!mockDirectory.isDirectory()) mockDirectory.mkdirs();

        assertTrue(mockDirectory.exists());

        File file = new File(mockDirectory, mockRandomFileName);

//        assertTrue(file.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BoxScore/" + mockRandomFileName + ".xls"));

        Uri fileUri = Uri.fromFile(file);

        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setLocale(new Locale("en", "EN"));

        assertTrue(workbookSettings.getLocale().getLanguage().equals("en"));
        assertTrue(workbookSettings.getLocale().getCountry().equals("EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings);
        WritableSheet sheetGameData = workbook.createSheet("GameData", 0);
        WritableSheet sheetGameInfo = workbook.createSheet("GameInfo", 1);

        sheetGameData.getCell(0, 0).getContents();
    }

    @Test
    public void onPostExecute() throws Exception {
    }

}