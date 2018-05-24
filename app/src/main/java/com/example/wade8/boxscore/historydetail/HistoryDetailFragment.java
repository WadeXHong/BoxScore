package com.example.wade8.boxscore.historydetail;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.customlayout.NonSwipeableViewPager;


public class HistoryDetailFragment extends Fragment implements HistoryDetailContract.View{

    private static final String TAG = HistoryDetailFragment.class.getSimpleName();

    private HistoryDetailContract.Presenter mPresenter;

    private NonSwipeableViewPager mViewPager;
    private TabLayout mTabLayout;
    private final int[] mTab = {R.string.teamBoxScore,R.string.playersBoxscore};

    public HistoryDetailFragment() {
    }

    public static HistoryDetailFragment newInstance() {
        return new HistoryDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_detail, container, false);

        mViewPager = view.findViewById(R.id.fragment_historydetail_viewpager);
        mTabLayout = view.findViewById(R.id.fragment_historydetail_tablelayout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mViewPager.getCurrentItem() == 0){
                    mViewPager.setSwipeable(true);
                }else {
                    mViewPager.setSwipeable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(HistoryDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void setViewPagerAdapter(ViewPagerFragmentAdapter viewPagerFragmentAdapter) {
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //set tab in tab layout
        for(int i=0; i<mTab.length;i++){
            mTabLayout.getTabAt(i).setText(mTab[i]);
        }
    }

    @Override
    public void setViewPagerPage() {
        mViewPager.setCurrentItem(0);
    }
}
