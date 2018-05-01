package com.example.wade8.boxscore.activities;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wade8.boxscore.BoxScoreContract;
import com.example.wade8.boxscore.R;

public class BoxScoreActivity extends AppCompatActivity implements BoxScoreContract.View {
    
    private static final String  TAG = BoxScoreActivity.class.getSimpleName();


    private ConstraintLayout mMainLayout;
    private ConstraintLayout mStartGameLayout;
    private ConstraintLayout mTeamManageLayout;
    private ConstraintLayout mGameHistoryLayout;
    private ConstraintLayout mSettingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_score);

        mMainLayout = findViewById(R.id.activity_boxscore_main_layout);
        mStartGameLayout = findViewById(R.id.activity_boxscore_startgame_layout);
        mTeamManageLayout = findViewById(R.id.activity_boxscore_teammanage_layout);
        mGameHistoryLayout = findViewById(R.id.activity_boxscore_gamehistory_layout);
        mSettingLayout = findViewById(R.id.activity_boxscore_setting_layout);
        mStartGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"StartGame pressed");
            }
        });
        mTeamManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"TeamManage pressed");
            }
        });
        mGameHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"GmaeHistory pressed");
            }
        });
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Setting pressed");
            }
        });


        init();
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);

    }

    private void setStatusBar(Activity activity) {

        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void setPresenter(BoxScoreContract.Presenter presenter) {

    }

    @Override
    public void showMainUi() {

    }

}
