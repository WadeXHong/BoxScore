package com.example.wade8.boxscore.gameboxscore;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade8.boxscore.Constants;
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
    private SparseArray<Integer> mTeamData;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private TextView mYourTeamScore;
    private TextView mOpponentTeamScore;
    private TextView mYourTeamFoul;
    private TextView mOpponentTeamfoul;
    private TextView mQuarter;
    private ImageView mUndo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_box_score);
        mViewPager = findViewById(R.id.activity_gameboxscore_viewpager);
        mTabLayout = findViewById(R.id.activity_gameboxscore_tablelayout);
        mYourTeamScore = findViewById(R.id.activity_gameboxscore_yourteamscore);
        mOpponentTeamScore = findViewById(R.id.activity_gameboxscore_opponentteamscore);
        mYourTeamFoul = findViewById(R.id.activity_gameboxscore_yourteamfoul);
        mOpponentTeamfoul = findViewById(R.id.activity_gameboxscore_opponentteamfoul);
        mQuarter = findViewById(R.id.activity_gameboxscore_quarter);
        mUndo = findViewById(R.id.activity_gameboxscore_undo);

        mGameInfo = (GameInfo) getIntent().getSerializableExtra("GameInfo");
        logTestingGameInfo();

        init();
        setOnClick();

//        mPresenter.writeInitDataIntoModel();

    }

    private void setOnClick() {

        mOpponentTeamScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mOpponentTeamScore onClick");
                mPresenter.pressOpponentTeamScore(mOpponentTeamScore.getText().toString());
            }
        });

        mYourTeamFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mYourTeamFoul onClick");
                mPresenter.pressYourTeamFoul(mYourTeamFoul.getText().toString());
            }
        });
        mOpponentTeamfoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mOpponentTeamFoul onClick");
                mPresenter.pressOpponentTeamFoul(mOpponentTeamfoul.getText().toString());
            }
        });
        mQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mQuarter onClick");
                mPresenter.pressQuarter(mQuarter.getText().toString());
            }
        });
        mUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mUndo onClick");
//                mPresenter.pressUndo();
            }
        });

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

    @Override
    public void setInitDataOnScreen(SparseArray<Integer> teamData) {
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

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }
}
