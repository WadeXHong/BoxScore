package com.example.wade8.boxscore.historymain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.GameHistoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryMainFragment extends Fragment implements HistoryMainContract.View{

    private static final String TAG = HistoryMainFragment.class.getSimpleName();

    private HistoryMainContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private GameHistoryAdapter mAdapter;

    public static HistoryMainFragment newInstance(){
        return new HistoryMainFragment();
    }

    public HistoryMainFragment() {
        // Required empty public constructor
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

        mRecyclerView = view.findViewById(R.id.fragment_historymain_recyclerview);
        mAdapter = new GameHistoryAdapter(mPresenter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setPresenter(HistoryMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
