package com.example.wade8.boxscore.startgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wade8.boxscore.R;

public class StartGameActivity extends AppCompatActivity implements StartGameContract.View{

    private static final String TAG = StartGameActivity.class.getSimpleName();

    private StartGameContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }

    @Override
    public void setPresenter(StartGameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainUi() {

    }

    @Override
    public void showTeamInSpinner() {

    }
}
