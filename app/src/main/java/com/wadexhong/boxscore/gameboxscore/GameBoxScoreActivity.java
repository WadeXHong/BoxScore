package com.wadexhong.boxscore.gameboxscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.customlayout.BSLinearLayout;
import com.wadexhong.boxscore.gesturelistener.OnScrollGestureListener;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.setting.SettingActivity;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.customlayout.BSViewPager;
import com.wadexhong.boxscore.dialogfragment.datastatistic.DataStatisticDialog;

public class GameBoxScoreActivity extends AppCompatActivity implements GameBoxScoreContract.View{

    private static final String TAG = GameBoxScoreActivity.class.getSimpleName();

    private GameBoxScoreContract.Presenter mPresenter;
    private final int PAGE_DATARECORD = 0;
    private final int PAGE_PLYERONCOURT = 1;
    private final int PAGE_DATASTATISTIC = 2;
    private final int[] mTab = {R.string.changePlayer,R.string.dataRecord,R.string.undoHistory};


    private GameInfo mGameInfo;
    private SparseIntArray mTeamData;

    private TabLayout mTabLayout;
    private BSViewPager mViewPager;
    private BSLinearLayout mOutestLinearLayout;

    private TextView mYourTeamScore;
    private TextView mOpponentTeamScore;
    private TextView mYourTeamFoul;
    private TextView mOpponentTeamfoul;
    private TextView mQuarter;
    private ImageView mUndo;
    private ImageView mDataStatistic;
    private ImageView mSave;
    private ImageView mSetting;


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
        setContentView(R.layout.activity_game_box_score);
        mOutestLinearLayout = findViewById(R.id.activity_gameboxscore_outestlayout);
        mViewPager = findViewById(R.id.activity_gameboxscore_viewpager);
        mTabLayout = findViewById(R.id.activity_gameboxscore_tablelayout);
        mYourTeamScore = findViewById(R.id.activity_gameboxscore_yourteamscore);
        mOpponentTeamScore = findViewById(R.id.activity_gameboxscore_opponentteamscore);
        mYourTeamFoul = findViewById(R.id.activity_gameboxscore_yourteamfoul);
        mOpponentTeamfoul = findViewById(R.id.activity_gameboxscore_opponentteamfoul);
        mQuarter = findViewById(R.id.activity_gameboxscore_quarter);
        mUndo = findViewById(R.id.activity_gameboxscore_undo);
        mDataStatistic = findViewById(R.id.activity_gameboxscore_datastatistic);
        mSave = findViewById(R.id.activity_gameboxscore_save);
        mSetting = findViewById(R.id.activity_gameboxscore_setting);

        init();

        logTestingGameInfo();
        setOnClick();
        setOnLongClick();
        setGesture();

//        mPresenter.writeInitDataIntoModel();

    }

    private void setGesture() {
        mOutestLinearLayout.setOnScrollGestureListener(new OnScrollGestureListener() {
            @Override
            public void ScrollUp(int pointerCount) {
                mPresenter.scrollUp(pointerCount);
            }

            @Override
            public void ScrollDown(int pointerCount) {
                mPresenter.scrollDown(pointerCount);
            }

            @Override
            public void ScrollLeft(int pointerCount) {
                mPresenter.scrollLeft(pointerCount);
            }

            @Override
            public void ScrollRight(int pointerCount) {
                mPresenter.scrollRight(pointerCount);
            }
        });
    }

    private void setOnClick() {

        mOpponentTeamScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mOpponentTeamScore onClick");
                mPresenter.pressOpponentTeamScore();
            }
        });

        mYourTeamFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mYourTeamFoul onClick");
                mPresenter.pressYourTeamFoul();
            }
        });
        mOpponentTeamfoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mOpponentTeamFoul onClick");
                mPresenter.pressOpponentTeamFoul();
            }
        });
        mQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mQuarter onClick");
                mPresenter.pressQuarter();
            }
        });
        mUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mUndo onClick");
                mPresenter.pressUndo();
            }
        });
        mDataStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mDataStatistic onClick");
                mPresenter.pressDataStatistic();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mSave onClick");
                new AlertDialog.Builder(GameBoxScoreActivity.this, R.style.OrangeDialog).setTitle("結束比賽")
                          .setMessage("比賽是否結束並儲存結果？")
                          .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.removeGameDataSharedPreferences();
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                          .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                              }
                          }).show();
            }
        });
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mSave onClick");
                startActivity(new Intent(GameBoxScoreActivity.this, SettingActivity.class));
            }
        });
    }

    private void setOnLongClick() {

        mOpponentTeamScore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.longPressOpponentTeamScore();
                return true;
            }
        });

        mOpponentTeamfoul.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.longPressOpponentTeamFoul();
                return true;
            }
        });

        mQuarter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.longPressQuarter();
                return true;
            }
        });
    }

    private void logTestingGameInfo() {
        Log.d(TAG,"GameInfo mGameName : " + mGameInfo.getGameName());
        Log.d(TAG,"GameInfo mOpponentName : " + mGameInfo.getOpponentName());
        Log.d(TAG,"GameInfo mMaxFoul : " + mGameInfo.getMaxFoul());
        Log.d(TAG,"GameInfo mTimeoutSecondHalf : " + mGameInfo.getTimeoutSecondHalf());
        Log.d(TAG,"GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getName());
        Log.d(TAG,"GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getNumber());
        Log.d(TAG,"GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getName());
        Log.d(TAG,"GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getNumber());
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new GameBoxScorePresenter(this, getSupportFragmentManager());
        mPresenter.checkIsResume(getIntent().getBooleanExtra("isResume",false));
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

    @Override
    public void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter) {
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        setTabInTabLayout();
        mViewPager.setCurrentItem(PAGE_PLYERONCOURT);
    }

    @Override
    public void setInitDataOnScreen(SparseIntArray teamData) {
        mTeamData = teamData;
        updateUiTeamData();
    }

    @Override
    public void updateUiTeamData() {
        mYourTeamScore.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE)));
        mOpponentTeamScore.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE)));
        mYourTeamFoul.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL)));
        mOpponentTeamfoul.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL)));
        mQuarter.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.QUARTER)));
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
    }

    @Override
    public void popDataStatisticDialog(DataStatisticDialog dialog) {
        dialog.show(getSupportFragmentManager(),"DataStatistic");
    }

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setGameInfoFromResume() {
        mGameInfo = mPresenter.resumeGameInfo((GameInfo) getIntent().getSerializableExtra("GameInfo"));
    }

    @Override
    public void setGameInfoFromInput() {
        mGameInfo = (GameInfo) getIntent().getSerializableExtra("GameInfo");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.OrangeDialog)
                  .setTitle(R.string.confirmExitDataRecord).setMessage(R.string.exitDataRecordMessage)
                  .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }})
                  .setNeutralButton("放棄比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          mPresenter.removeGameDataInDataBase();
                          mPresenter.removeGameDataSharedPreferences();
                          dialog.dismiss(); //TODO 清除DataBase
                          GameBoxScoreActivity.super.onBackPressed();
                      }
                  })
                  .setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          GameBoxScoreActivity.super.onBackPressed();
                      }
                  }).show();
    }
}
