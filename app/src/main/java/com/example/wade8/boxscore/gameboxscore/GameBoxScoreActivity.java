package com.example.wade8.boxscore.gameboxscore;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wade8.boxscore.BoxScorePresenter;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.objects.GameInfo;

public class GameBoxScoreActivity extends AppCompatActivity implements GameBoxScoreContract.View{

    private static final String TAG = GameBoxScoreActivity.class.getSimpleName();

    private GameBoxScoreContract.Presenter mPresenter;
    private final int PAGE_DATARECORD = 0;
    private final int PAGE_PLYERONCOURT = 1;
    private final int PAGE_DATASTATISTIC = 2;
    private final int[] mTab = {R.string.dataRecord,R.string.playerOnCourt,R.string.dataStatistic};


    private GameInfo mGameInfo;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_box_score);
        mViewPager = findViewById(R.id.activity_gameboxscore_viewpager);
        mTabLayout = findViewById(R.id.activity_gameboxscore_tablelayout);

        mGameInfo = (GameInfo) getIntent().getSerializableExtra("GameInfo");
        logTestingGameInfo();

        init();
    }

    private void logTestingGameInfo() {
        Log.d(TAG,"GameInfo mGameName : " + mGameInfo.getGameName());
        Log.d(TAG,"GameInfo mOpponentName : " + mGameInfo.getOpponentName());
        Log.d(TAG,"GameInfo mMaxFoul : " + mGameInfo.getMaxFoul());
        Log.d(TAG,"GameInfo mTimeoutSecondHalf : " + mGameInfo.getTimeoutSecondHalf());
        Log.d(TAG,"GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getmName());
        Log.d(TAG,"GameInfo mStartingPlayList : " + mGameInfo.getStartingPlayerList().get(0).getmNumber());
        Log.d(TAG,"GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getmName());
        Log.d(TAG,"GameInfo mSubstitutePlayerList : " + mGameInfo.getSubstitutePlayerList().get(0).getmNumber());
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new GameBoxScorePresenter(this, getSupportFragmentManager());
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

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }
}
