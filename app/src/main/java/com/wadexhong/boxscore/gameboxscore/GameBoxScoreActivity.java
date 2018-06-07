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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.customlayout.BSLinearLayout;
import com.wadexhong.boxscore.gesturelistener.OnScrollGestureListener;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.setting.SettingActivity;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.customlayout.BSViewPager;
import com.wadexhong.boxscore.gameboxscore.datastatistic.DataStatisticDialog;

public class GameBoxScoreActivity extends AppCompatActivity implements GameBoxScoreContract.View {

    private final String TAG = GameBoxScoreActivity.class.getSimpleName();

    private GameBoxScoreContract.Presenter mPresenter;
    private final int PAGE_DATARECORD = 0;
    private final int PAGE_PLYERONCOURT = 1;
    private final int PAGE_DATASTATISTIC = 2;
    private final int[] mTab = {R.string.changePlayer, R.string.dataRecord, R.string.undoHistory};


    private GameInfo mGameInfo;
    private SparseIntArray mTeamData;

    private TabLayout mTabLayout;
    private BSViewPager mViewPager;
    private BSLinearLayout mOutermostLinearLayout;

    private TextView mYourTeamScoreTextView;
    private TextView mOpponentTeamScoreTextView;
    private TextView mYourTeamFoulTextView;
    private TextView mOpponentTeamfoulTextView;
    private TextView mQuarterTextView;
    private ImageView mUndoImageView;
    private ImageView mDataStatisticImageView;
    private ImageView mSaveImageView;
    private ImageView mSettingImageView;

    private LinearLayout mOpponentTeamScoreAdjustLayout;
    private LinearLayout mOpponentTeamFoulAdjustLayout;
    private ImageView mOpponentTeamScorePlusImageView;
    private ImageView mOpponentTeamScoreMinusImageView;
    private ImageView mOpponentTeamFoulPlusImageView;
    private ImageView mOpponentTeamFoulMinusImageView;


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

        mOutermostLinearLayout = findViewById(R.id.activity_gameboxscore_outestlayout);
        mViewPager = findViewById(R.id.activity_gameboxscore_viewpager);
        mTabLayout = findViewById(R.id.activity_gameboxscore_tablelayout);
        mYourTeamScoreTextView = findViewById(R.id.activity_gameboxscore_yourteamscore);
        mOpponentTeamScoreTextView = findViewById(R.id.activity_gameboxscore_opponentteamscore);
        mYourTeamFoulTextView = findViewById(R.id.activity_gameboxscore_yourteamfoul);
        mOpponentTeamfoulTextView = findViewById(R.id.activity_gameboxscore_opponentteamfoul);
        mQuarterTextView = findViewById(R.id.activity_gameboxscore_quarter);
        mUndoImageView = findViewById(R.id.activity_gameboxscore_undo);
        mDataStatisticImageView = findViewById(R.id.activity_gameboxscore_datastatistic);
        mSaveImageView = findViewById(R.id.activity_gameboxscore_save);
        mSettingImageView = findViewById(R.id.activity_gameboxscore_setting);
        mOpponentTeamScoreAdjustLayout = findViewById(R.id.activity_gameboxscore_opponentteamscore_adjust);
        mOpponentTeamScorePlusImageView = findViewById(R.id.activity_gameboxscore_opponentteamscore_plus);
        mOpponentTeamScoreMinusImageView = findViewById(R.id.activity_gameboxscore_opponentteamscore_minus);
        mOpponentTeamFoulAdjustLayout = findViewById(R.id.activity_gameboxscore_opponentteamfoul_adjust);
        mOpponentTeamFoulPlusImageView = findViewById(R.id.activity_gameboxscore_opponentteamfoul_plus);
        mOpponentTeamFoulMinusImageView = findViewById(R.id.activity_gameboxscore_opponentteamfoul_minus);

        init();

        setOnClick();
        setGesture();
        //長按功能不直觀，暫時取消
//        setOnLongClick();
        logTestingGameInfo();
    }

    private void init() {
        Log.i(TAG, "BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new GameBoxScorePresenter(this, getSupportFragmentManager());
        mPresenter.checkIsResume(getIntent().getBooleanExtra("isResume", false));
        mPresenter.start();
    }

    private void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setOnClick() {

        mOpponentTeamScoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamScoreTextView onClick");
                BoxScore.vibrate();
                mPresenter.pressOpponentTeamScore();
            }
        });
        mYourTeamFoulTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mYourTeamFoulTextView onClick");
                BoxScore.vibrate();
                mPresenter.pressYourTeamFoul();
            }
        });
        mOpponentTeamfoulTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamFoul onClick");
                BoxScore.vibrate();
                mPresenter.pressOpponentTeamFoul();
            }
        });
        mQuarterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mQuarterTextView onClick");
                BoxScore.vibrate();

                new AlertDialog.Builder(GameBoxScoreActivity.this, R.style.OrangeDialog).setTitle(R.string.ask_next_quarter_title)
                          .setMessage(R.string.ask_next_quarter_message)
                          .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  BoxScore.vibrate();
                                  mPresenter.pressQuarter();
                              }
                          })
                          .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.cancel();
                              }
                          }).show();
            }
        });
        mUndoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mUndoImageView onClick");
                BoxScore.vibrate();
                mPresenter.pressUndo();
            }
        });
        mDataStatisticImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mDataStatisticImageView onClick");
                BoxScore.vibrate();
                mPresenter.pressDataStatistic();
            }
        });
        mSaveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                Log.d(TAG, "mSaveImageView onClick");
                new AlertDialog.Builder(GameBoxScoreActivity.this, R.style.OrangeDialog).setTitle(R.string.ask_save_title)
                          .setMessage(R.string.ask_save_message)
                          .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  mPresenter.saveAndEndCurrentGame();
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
        mSettingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                Log.d(TAG, "mSettingImageView onClick");
                startActivity(new Intent(GameBoxScoreActivity.this, SettingActivity.class));
            }
        });
        mOpponentTeamScorePlusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamScorePlusImageView onClick");
                BoxScore.vibrate();
                mPresenter.pressOpponentTeamScore();
            }
        });
        mOpponentTeamScoreMinusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamScoreMinusImageView onClick");
                BoxScore.vibrate();
                mPresenter.longPressOpponentTeamScore();
            }
        });
        mOpponentTeamFoulPlusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamFoulPlusImageView onClick");
                BoxScore.vibrate();
                mPresenter.pressOpponentTeamFoul();
            }
        });
        mOpponentTeamFoulMinusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mOpponentTeamFoulMinusImageView onClick");
                BoxScore.vibrate();
                mPresenter.longPressOpponentTeamFoul();
            }
        });
    }

    @Deprecated
    private void setOnLongClick() {

        mOpponentTeamScoreTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BoxScore.vibrate();
                mPresenter.longPressOpponentTeamScore();
                return true;
            }
        });
        mOpponentTeamfoulTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BoxScore.vibrate();
                mPresenter.longPressOpponentTeamFoul();
                return true;
            }
        });
        mQuarterTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BoxScore.vibrate();
                mPresenter.longPressQuarter();
                return true;
            }
        });
    }

    private void setGesture() {
        mOutermostLinearLayout.setOnScrollGestureListener(new OnScrollGestureListener() {
            @Override
            public void ScrollUp(int pointerCount) {
                BoxScore.vibrate();
                mPresenter.scrollUp(pointerCount);
            }

            @Override
            public void ScrollDown(int pointerCount) {
                BoxScore.vibrate();
                mPresenter.scrollDown(pointerCount);
            }

            @Override
            public void ScrollLeft(int pointerCount) {
                BoxScore.vibrate();
                mPresenter.scrollLeft(pointerCount);
            }

            @Override
            public void ScrollRight(int pointerCount) {
                BoxScore.vibrate();
                mPresenter.scrollRight(pointerCount);
            }
        });
    }

    private void logTestingGameInfo() {
        Log.d(TAG, "GameInfo mGameName : " + mGameInfo.getGameName());
        Log.d(TAG, "GameInfo mOpponentName : " + mGameInfo.getOpponentName());
        Log.d(TAG, "GameInfo mMaxFoul : " + mGameInfo.getMaxFoul());
        Log.d(TAG, "GameInfo mTimeoutSecondHalf : " + mGameInfo.getTimeoutSecondHalf());
        Log.d(TAG, "GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getName());
        Log.d(TAG, "GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getNumber());
        Log.d(TAG, "GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getName());
        Log.d(TAG, "GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getNumber());
    }

    @Override
    public GameInfo getGameInfo() {
        return mGameInfo;
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
    public void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter) {
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        setTabInTabLayout();
        mViewPager.setCurrentItem(PAGE_PLYERONCOURT);
    }

    private void setTabInTabLayout() {
        for (int i = 0; i < mTab.length; i++) {
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }

    @Override
    public void setInitDataOnScreen(SparseIntArray teamData) {
        mTeamData = teamData;
        updateUiTeamData();
    }

    @Override
    public void updateUiTeamData() {
        //TODO TIMEOUTS
        mYourTeamScoreTextView.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.YOUR_TEAM_TOTAL_SCORE)));
        mOpponentTeamScoreTextView.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_TOTAL_SCORE)));
        mYourTeamFoulTextView.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.YOUR_TEAM_FOUL)));
        mOpponentTeamfoulTextView.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.OPPONENT_TEAM_FOUL)));
        mQuarterTextView.setText(String.valueOf(mTeamData.get(Constants.RecordDataType.QUARTER)));
    }

    @Override
    public void popDataStatisticDialog(DataStatisticDialog dialog) {
        dialog.show(getSupportFragmentManager(), "DataStatistic");
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        BoxScore.vibrate();
        new AlertDialog.Builder(this, R.style.OrangeDialog)
                  .setTitle(R.string.confirmExitDataRecord).setMessage(R.string.exitDataRecordMessage)
                  .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  })
                  .setNeutralButton(R.string.discardGame, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                          mPresenter.removeGameDataInDataBase();
                          mPresenter.removeGameDataSharedPreferences();

                          dialog.dismiss(); //TODO 清除DataBase
                          GameBoxScoreActivity.super.onBackPressed();
                      }
                  })
                  .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          GameBoxScoreActivity.super.onBackPressed();
                      }
                  }).show();
    }

    @Override
    public void setPresenter(GameBoxScoreContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
