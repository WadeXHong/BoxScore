package com.bardxhong.boxscore.startgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.bardxhong.boxscore.customlayout.BSViewPager;
import com.bardxhong.boxscore.gameboxscore.GameBoxScoreActivity;
import com.bardxhong.boxscore.teammanage.TeamManageActivity;

public class StartGameActivity extends AppCompatActivity implements StartGameContract.View {

    private final String TAG = StartGameActivity.class.getSimpleName();

    private StartGameContract.Presenter mPresenter;

    public static final String EXTRA_GAME_INFO = "GameInfo";
    private final int PAGE_GAME_NAME = 0;
    private final int PAGE_PLAYER = 1;
    private final int PAGE_DETAIL = 2;

    private Toolbar mToolbar;
    private TextView mStartGameTextView;
    private ImageView mNextPageImageView;
    private BSViewPager mViewPager;
    private TabLayout mTabLayout;
    private View.OnClickListener onClickTransToGameNameSetting, onClickTransToPlayerSetting, onClickTransToDetailSetting;

    private float mInitPositionX;
    private boolean mIsTurnRight = false;
    private boolean mIsChangePageAllowed = false;

    private final int[] mTabStringId = {R.string.gamenamesetting, R.string.playerlist, R.string.detailsetting};

    @Override
    protected void onResume() {
        super.onResume();

        if (BoxScore.sBrightness != -1) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = BoxScore.sBrightness;
            getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mViewPager = findViewById(R.id.activity_startgame_viewpager);
        mTabLayout = findViewById(R.id.activity_startgame_tablelayout);

        mPresenter = new StartGamePresenter(this, getSupportFragmentManager());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.onPageSelected(position);

                //從頭檢查輸入
                for (int i = 0; i < position; i++) {
                    mPresenter.checkInput(i);
                    if (!mIsChangePageAllowed) {
                        mTabLayout.getTabAt(i).select();
//                        mViewPager.setCurrentItem(i);
                        popInputIllegalDialog();
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mPresenter.checkInput(mViewPager.getCurrentItem());
                }
            }
        });

        setOnClickParameters();
        setToolbar();
    }

    private void setOnClickParameters() {

        onClickTransToGameNameSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_GAME_NAME);
            }
        };

        onClickTransToPlayerSetting = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_PLAYER);
            }
        };

        onClickTransToDetailSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_DETAIL);
            }
        };
    }

    private void setTabInTabLayout() {
        for (int i = 0; i < mTabStringId.length; i++) {
            mTabLayout.getTabAt(i).setText(mTabStringId[i]);
        }
    }

    private void setToolbar() {

        mToolbar = findViewById(R.id.activity_startgame_toolbar);
        mNextPageImageView = mToolbar.findViewById(R.id.activity_boxscore_nextpage);
        mStartGameTextView = mToolbar.findViewById(R.id.activity_boxscore_startgamebutton);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mNextPageImageView.setOnClickListener(onClickTransToPlayerSetting);
        mStartGameTextView.setVisibility(View.GONE);

        mStartGameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BoxScore.sIsOnClickAllowed) {
                    Log.d(TAG, "Game Start");
                    Intent intent = new Intent(StartGameActivity.this, GameBoxScoreActivity.class);
                    intent.putExtra(StartGameActivity.EXTRA_GAME_INFO, mPresenter.getSettingResult(new GameInfo()));
                    startActivity(intent);
                    StartGameActivity.this.finish();
                }
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter) {
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        setTabInTabLayout();
    }

    /**
     * Override dispatchTouchEvent in order to  recognize the scrolling direction is left or right at the very first,
     * which provided {@link #setViewPagerCurrentItem} judgment about showing IlleagalDialog or not.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getActionMasked();
        float dx = ev.getX(0) - mInitPositionX;

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                mInitPositionX = ev.getX(0);
                break;

            case MotionEvent.ACTION_MOVE:
                if (dx < 0) {
                    mIsTurnRight = true;
                } else {
                    mIsTurnRight = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (dx < 0) {
                    mIsTurnRight = true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setViewPagerCurrentItem(boolean isChangePageAllowed) {
        mIsChangePageAllowed = isChangePageAllowed;
        mViewPager.setScrollAllowed(isChangePageAllowed);

        if (mIsTurnRight && !isChangePageAllowed) {
            popInputIllegalDialog();
            mIsTurnRight = false;
        }
    }

    private void popInputIllegalDialog() {

        new AlertDialog.Builder(this, R.style.OrangeDialog)
                  .setTitle(R.string.start_game_illegal_title)
                  .setMessage(R.string.start_game_illegal_message)
                  .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    @Override
    public void noLegalTeam() {

        new AlertDialog.Builder(this, R.style.OrangeDialog)
                  .setTitle(R.string.no_legal_team_title)
                  .setMessage(R.string.no_legal_team_message)
                  .setCancelable(false)
                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          startActivity(new Intent(StartGameActivity.this, TeamManageActivity.class));
                          finish();
                          dialog.cancel();
                      }
                  })
                  .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          finish();
                          dialog.cancel();
                      }
                  }).show();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this, R.style.OrangeDialog)
                  .setTitle(R.string.confirmGoBack)
                  .setMessage(R.string.goBackConfirmMessage)
                  .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  })
                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          StartGameActivity.super.onBackPressed();
                      }
                  }).show();
    }

    @Override
    public void setGameNameSettingToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mNextPageImageView.setVisibility(View.VISIBLE);
        mStartGameTextView.setVisibility(View.GONE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mNextPageImageView.setOnClickListener(onClickTransToPlayerSetting);
    }

    @Override
    public void setPlayerListToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_30dp);
        mNextPageImageView.setVisibility(View.VISIBLE);
        mStartGameTextView.setVisibility(View.GONE);

        mToolbar.setNavigationOnClickListener(onClickTransToGameNameSetting);
        mNextPageImageView.setOnClickListener(onClickTransToDetailSetting);
    }

    @Override
    public void setDetailSettingToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_30dp);
        mNextPageImageView.setVisibility(View.GONE);
        mStartGameTextView.setVisibility(View.VISIBLE);

        mToolbar.setNavigationOnClickListener(onClickTransToPlayerSetting);
    }

    @Override
    public void setPresenter(StartGameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainUi() {

    }
}
