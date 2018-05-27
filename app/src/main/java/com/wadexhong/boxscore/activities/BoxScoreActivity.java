package com.wadexhong.boxscore.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.BoxScoreContract;
import com.wadexhong.boxscore.BoxScorePresenter;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity;
import com.wadexhong.boxscore.gamehistory.GameHistoryActivity;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.setting.SettingActivity;
import com.wadexhong.boxscore.startgame.StartGameActivity;
import com.wadexhong.boxscore.teammanage.TeamManageActivity;

public class BoxScoreActivity extends AppCompatActivity implements BoxScoreContract.View {
    
    private static final String  TAG = BoxScoreActivity.class.getSimpleName();
    private Context mContext;

    private BoxScoreContract.Presenter mPresenter;

    private ConstraintLayout mMainLayout;
    private ConstraintLayout mStartGameLayout;
    private ConstraintLayout mTeamManageLayout;
    private ConstraintLayout mGameHistoryLayout;
    private ConstraintLayout mSettingLayout;

    @Override
    protected void onResume() {
        super.onResume();
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = BoxScore.sBrightness;
            getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_score);
        mContext = this;

        mMainLayout = findViewById(R.id.activity_boxscore_main_layout);
        mStartGameLayout = findViewById(R.id.activity_boxscore_startgame_layout);
        mTeamManageLayout = findViewById(R.id.activity_boxscore_teammanage_layout);
        mGameHistoryLayout = findViewById(R.id.activity_boxscore_gamehistory_layout);
        mSettingLayout = findViewById(R.id.activity_boxscore_setting_layout);
        mStartGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"StartGame pressed");
                mPresenter.pressStartGame();
            }
        });
        mTeamManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"TeamManage pressed");
                startActivity(new Intent(BoxScoreActivity.this, TeamManageActivity.class));
            }
        });
        mGameHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"GmaeHistory pressed");
                startActivity(new Intent(BoxScoreActivity.this, GameHistoryActivity.class));
            }
        });
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Setting pressed");
                startActivity(new Intent(BoxScoreActivity.this, SettingActivity.class));
            }
        });


        init();
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new BoxScorePresenter(this);
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
    public void setPresenter(BoxScoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainUi() {

    }

    @Override
    public void askResumeGame(String opponentName) {
        new AlertDialog.Builder(this,R.style.OrangeDialog).setTitle("恢復比賽")
                  .setMessage("上次與\n"+opponentName+" 的比賽記錄尚未結束\n是否恢復比賽？\n\n警告：選擇\n「放棄並開新比賽」\n將刪除紀錄")
                  .setPositiveButton("恢復比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          transToGameBoxScore();//TODO ??????????? How to do ?
                          Log.d(TAG,"pressed Resume");
                          dialog.dismiss();
                      }})
                  .setNegativeButton("結束並開新比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG,"pressed End and start new");
                          mPresenter.removeGameDataSharedPreferences();
                          transToStartGame();
                          dialog.dismiss();
                      }})
                  .setNeutralButton("放棄並開新比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG,"pressed Discard and start new");
                          //ToDo callPresenter to delete previous gameData in DB
                          mPresenter.removeGameDataInDataBase();
                          mPresenter.removeGameDataSharedPreferences();
                          transToStartGame();
                          dialog.dismiss();
                      }}).show();

    }

    @Override
    public void transToStartGame() {
        startActivity(new Intent(mContext, StartGameActivity.class));
    }

    @Override
    public void transToGameBoxScore() {
        startActivity(new Intent(this, GameBoxScoreActivity.class).putExtra("GameInfo",new GameInfo()).putExtra("isResume",true));

    }

}
