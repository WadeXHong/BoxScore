package com.example.wade8.boxscore;

import android.database.Cursor;

/**
 * Created by wade8 on 2018/5/1.
 */

public class BoxScorePresenter implements BoxScoreContract.Presenter {

    private static final String TAG =BoxScorePresenter.class.getSimpleName();

    private final BoxScoreContract.View mBoxScoreView;

    public BoxScorePresenter(BoxScoreContract.View boxScoreView) {
        this.mBoxScoreView = boxScoreView;
        mBoxScoreView.setPresenter(this);
    }


    @Override
    public void start() {

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
        if (SharedPreferenceHelper.contains(SharedPreferenceHelper.PLAYING_GAME)){
            String string = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,"87");
            Cursor cursor = BoxScore.getGameInfoDbHelper()
                      .getReadableDatabase()
                      .query(Constants.GameInfoDBContract.TABLE_NAME
                                ,null
                                ,Constants.GameInfoDBContract.GAME_ID + " = ?"
                                ,new String[]{SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,"")},null,null,null);
            cursor.moveToFirst();
            mBoxScoreView.askResumeGame(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_NAME)));
            cursor.close();
        }else {
            transToStartGame();
        }
    }

    @Override
    public void clearPreviousGameData() {

    }
}
