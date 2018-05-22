package com.example.wade8.boxscore.gamehistory;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wade8.boxscore.R;

public class GameHistoryActivity extends AppCompatActivity implements GameHistoryContract.View{

    private static final String TAG = GameHistoryActivity.class.getSimpleName();

    private GameHistoryContract.Presenter mPresenter;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        mToolbar = findViewById(R.id.activity_gamehistory_toolbar);
        setGameHistoryToolBar();

        init();
    }

    @Override
    public void setGameHistoryToolBar() {
        mToolbar.setTitle("歷史紀錄");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        Log.i(TAG,"GameHistoryActivity.init");
        mPresenter = new GameHistoryPresenter(this, getSupportFragmentManager());
        mPresenter.start();

    }




    @Override
    public void setPresenter(GameHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
