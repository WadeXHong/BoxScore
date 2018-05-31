package com.wadexhong.boxscore;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wadexhong.boxscore.activities.BoxScoreActivity;
import com.wadexhong.boxscore.firebasemodel.Get;

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
        if (SharedPreferenceHelper.contains(SharedPreferenceHelper.PLAYING_GAME)){
            String string = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,"87");
            Cursor cursor = BoxScore.getGameInfoDbHelper()
                      .getReadableDatabase()
                      .query(Constants.GameInfoDBContract.TABLE_NAME
                                ,null
                                ,Constants.GameInfoDBContract.GAME_ID + " = ?"
                                ,new String[]{SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME,"")},null,null,null);
            cursor.moveToFirst();
            try{
                mBoxScoreView.askResumeGame(cursor.getString(cursor.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_NAME)));
            }catch (CursorIndexOutOfBoundsException e){
                Log.w(TAG, e.getMessage());
            }
            cursor.close();
        }else {
            transToStartGame();
        }
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
    public void signUpFireBase(String userEmail, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener((BoxScoreActivity)BoxScore.getAppContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.w("","");
                }
            }
        });
    }

    @Override
    public void logInFireBase(String userEmail, String password) {

    }

    @Override
    public void updateDbFromFireBase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Get.onCreate();
        }
    }
}
