package com.example.wade8.boxscore.teamplayers;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.TeamPlayersAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamPlayersFragment extends Fragment implements TeamPlayersContract.View{

    private static final String TAG = TeamPlayersFragment.class.getSimpleName();

    private TeamPlayersContract.Presenter mPresenter;

    private ConstraintLayout mCreatePlayerLayout;
    private RecyclerView mRecyclerView;
    private TeamPlayersAdapter mAdapter;

    private String mTeamId;

    public static TeamPlayersFragment newInstance(){
        return new TeamPlayersFragment();
    }
    public TeamPlayersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_players, container, false);

        mCreatePlayerLayout = view.findViewById(R.id.fragment_teamplayer_createplayer_layout);
        mRecyclerView = view.findViewById(R.id.fragment_teamplayer_recyclerview);
        mAdapter = new TeamPlayersAdapter(mTeamId, mPresenter);


        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(TeamPlayersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }

    public void refreshUi(){
        mAdapter.refreshCursor();
    }
}
