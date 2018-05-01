package com.example.wade8.boxscore.startgame;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;

public class StartGameActivity extends AppCompatActivity implements StartGameContract.View{

    private static final String TAG = StartGameActivity.class.getSimpleName();

    private StartGameContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int[] mTab = {R.string.gamenamesetting,R.string.playerlist,R.string.detailsetting};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mViewPager = findViewById(R.id.activity_startgame_viewpager);
        mTabLayout = findViewById(R.id.activity_startgame_tablelayout);

        mPresenter = new StartGamePresenter(this, getSupportFragmentManager());
//        mPresenter.set();


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
        setTabInTabLayout();
    }

    private void setTabInTabLayout() {
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }
}
