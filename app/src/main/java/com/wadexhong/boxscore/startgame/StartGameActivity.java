package com.wadexhong.boxscore.startgame;

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

import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.customlayout.BSViewPager;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity;

public class StartGameActivity extends AppCompatActivity implements StartGameContract.View{

    private static final String TAG = StartGameActivity.class.getSimpleName();

    private StartGameContract.Presenter mPresenter;
    private final int PAGE_GAMENAME = 0;
    private final int PAGE_PLAYER = 1;
    private final int PAGE_DETAIL = 2;

    private GameInfo mGameInfo;

    private Toolbar mToolbar;
    private TextView mStartGame;
    private ImageView mNextPage;
    private BSViewPager mViewPager;
    private TabLayout mTabLayout;
    private View.OnClickListener onClickTransToGameNameSetting,onClickTransToPlayerSetting,onClickTransToDetailSetting;

    private float mInitPositionX;
    private boolean mIsTurnRight = false;
    private boolean mIsChangePageAllowed = false;

    private final int[] mTab = {R.string.gamenamesetting,R.string.playerlist,R.string.detailsetting};

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
                for (int i=0; i<position; i++){
                    mPresenter.checkInput(i);
                    if (!mIsChangePageAllowed){
                        mTabLayout.getTabAt(i).select();
//                        mViewPager.setCurrentItem(i);
                        popInputIllegalDialog();
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING){
                    mPresenter.checkInput(mViewPager.getCurrentItem());
                }
            }
        });

        setOnClickParameters();
        setToolbar();

        mGameInfo = new GameInfo();

//        mPresenter.set();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        float dx = ev.getX(0) - mInitPositionX;
        switch (action){

            case MotionEvent.ACTION_DOWN:
                mInitPositionX = ev.getX(0);
                break;

            case MotionEvent.ACTION_MOVE:
                if (dx < 0) {
                    mIsTurnRight = true;
                }else {
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

    private void setOnClickParameters() {

        onClickTransToGameNameSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_GAMENAME);
//                mPresenter.transToGameNameSettingPage();
            }
        };

        onClickTransToPlayerSetting = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_PLAYER);
//                mPresenter.transToPlayerSettingPage();
            }
        };

        onClickTransToDetailSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PAGE_DETAIL);
//                mPresenter.transToDetailSettingPage();
            }
        };
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.activity_startgame_toolbar);
        mNextPage = mToolbar.findViewById(R.id.activity_boxscore_nextpage);
        mNextPage.setOnClickListener(onClickTransToPlayerSetting);
        mStartGame = mToolbar.findViewById(R.id.activity_boxscore_startgamebutton);
        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Game Start");
                Intent intent = new Intent(StartGameActivity.this, GameBoxScoreActivity.class);
                intent.putExtra("GameInfo", mPresenter.getSettingResult(new GameInfo()));//TODO GameInfo form real input
                startActivity(intent);
//                writeIntoDB();
                StartGameActivity.this.finish();
            }
        });
        mStartGame.setVisibility(View.GONE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    @Override
    public void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter) {
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        setTabInTabLayout();
    }

    @Override
    public void setGameNameSettingToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mNextPage.setOnClickListener(onClickTransToPlayerSetting);
        mNextPage.setVisibility(View.VISIBLE);
        mStartGame.setVisibility(View.GONE);
    }

    @Override
    public void setPlayerListToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_30dp);
        mToolbar.setNavigationOnClickListener(onClickTransToGameNameSetting);
        mNextPage.setOnClickListener(onClickTransToDetailSetting);
        mNextPage.setVisibility(View.VISIBLE);
        mStartGame.setVisibility(View.GONE);
    }

    @Override
    public void setDetailSettingToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_30dp);
        mToolbar.setNavigationOnClickListener(onClickTransToPlayerSetting);
        mNextPage.setVisibility(View.GONE);
        mStartGame.setVisibility(View.VISIBLE);
    }

    @Override
    public void setViewPagerCurrentItem(boolean isChangePageAllowed) {
        mIsChangePageAllowed = isChangePageAllowed;
        mViewPager.setScrollAllowed(isChangePageAllowed);
        if (mIsTurnRight && !isChangePageAllowed) {
            popInputIllegalDialog();
            mIsTurnRight = false;
//        mViewPager.setCurrentItem(position);
        }

    }

    private void popInputIllegalDialog() {
        new AlertDialog.Builder(this).setTitle("輸入未完成").setMessage("\n必填項目不符合規定").setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.OrangeDialog)
                  .setTitle(R.string.confirmGoBack).setMessage(R.string.goBackConfirmMessage)
                  .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }})
                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          StartGameActivity.super.onBackPressed();
                      }
                  }).show();
    }
}
