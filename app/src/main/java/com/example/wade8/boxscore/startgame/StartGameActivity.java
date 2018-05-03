package com.example.wade8.boxscore.startgame;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

public class StartGameActivity extends AppCompatActivity implements StartGameContract.View{

    private static final String TAG = StartGameActivity.class.getSimpleName();

    private StartGameContract.Presenter mPresenter;
    private final int PAGE_GAMENAME = 0;
    private final int PAGE_PLAYER = 1;
    private final int PAGE_DETAIL = 2;

    private Toolbar mToolbar;
    private TextView mStartGame;
    private ImageView mNextPage;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private View.OnClickListener onClickTransToGameNameSetting,onClickTransToPlayerSetting,onClickTransToDetailSetting;

    private final int[] mTab = {R.string.gamenamesetting,R.string.playerlist,R.string.detailsetting};

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setOnClickParameters();
        setToolbar();

//        mPresenter.set();


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

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }


}
