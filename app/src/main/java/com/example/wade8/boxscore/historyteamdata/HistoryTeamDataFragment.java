package com.example.wade8.boxscore.historyteamdata;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.HistoryTeamDataAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTeamDataFragment extends Fragment implements HistoryTeamDataContract.View{

    private static final String TAG = HistoryTeamDataFragment.class.getSimpleName();

    private HistoryTeamDataContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private HistoryTeamDataAdapter mAdapter;
    private String mGameId;

    public static HistoryTeamDataFragment newInstance(String gameId){
        HistoryTeamDataFragment fragment = new HistoryTeamDataFragment();
        Bundle args = new Bundle();
        args.putString("gameId", gameId);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryTeamDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mGameId = getArguments().getString("gameId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_team_data, container, false);

        mRecyclerView = view.findViewById(R.id.fragment_history_teamdata_recyclerview);
        mAdapter = new HistoryTeamDataAdapter(mPresenter, mGameId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setPresenter(HistoryTeamDataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void refreshUi(String gameId) {
        mGameId = gameId;
        mAdapter.refreshCursor(gameId);
    }
}
