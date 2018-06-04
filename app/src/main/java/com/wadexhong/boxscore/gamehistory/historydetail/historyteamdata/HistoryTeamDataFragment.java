package com.wadexhong.boxscore.gamehistory.historydetail.historyteamdata;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTeamDataFragment extends Fragment implements HistoryTeamDataContract.View{

    private static final String TAG = HistoryTeamDataFragment.class.getSimpleName();

    private HistoryTeamDataContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private String mGameId;

    public static HistoryTeamDataFragment newInstance(){
        HistoryTeamDataFragment fragment = new HistoryTeamDataFragment();
        return fragment;
    }

    public HistoryTeamDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_team_data, container, false);

        mRecyclerView = view.findViewById(R.id.fragment_history_teamdata_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPresenter.setAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void setPresenter(HistoryTeamDataContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void setAdapter(HistoryTeamDataAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void scrollToTop() {
        if (mRecyclerView != null)
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }
}
