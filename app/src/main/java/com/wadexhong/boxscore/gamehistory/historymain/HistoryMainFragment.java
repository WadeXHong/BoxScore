package com.wadexhong.boxscore.gamehistory.historymain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadexhong.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryMainFragment extends Fragment implements HistoryMainContract.View {

    private static final String TAG = HistoryMainFragment.class.getSimpleName();

    private HistoryMainContract.Presenter mPresenter;

    private RecyclerView mGameHistoryRecyclerView;
    private HistoryMainAdapter mHistoryMainAdapter;

    public HistoryMainFragment() {
        // Required empty public constructor
    }

    public static HistoryMainFragment newInstance() {
        return new HistoryMainFragment();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) mPresenter.setGameHistoryToolBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_main, container, false);

        mGameHistoryRecyclerView = view.findViewById(R.id.fragment_historymain_recyclerview);
        mHistoryMainAdapter = new HistoryMainAdapter(mPresenter);
        mGameHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGameHistoryRecyclerView.setAdapter(mHistoryMainAdapter);

        return view;
    }

    @Override
    public void setPresenter(HistoryMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
