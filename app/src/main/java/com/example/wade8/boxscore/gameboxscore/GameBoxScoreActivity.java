package com.example.wade8.boxscore.gameboxscore;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wade8.boxscore.BoxScorePresenter;
import com.example.wade8.boxscore.R;

public class GameBoxScoreActivity extends AppCompatActivity implements GameBoxScoreContract.View{

    private static final String TAG = GameBoxScoreActivity.class.getSimpleName();

    private GameBoxScoreContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_box_score);

        init();
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new GameBoxScorePresenter(this);
        mPresenter.start();

    }


    private void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void setPresenter(GameBoxScoreContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
