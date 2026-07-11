package com.wadexhong.boxscore.legacy.teammanage.teamplayers;


import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadexhong.boxscore.R;

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) mAdapter.refreshCursor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_players, container, false);

        mCreatePlayerLayout = view.findViewById(R.id.fragment_teamplayer_createplayer_layout);
        mRecyclerView = view.findViewById(R.id.fragment_teamplayer_recyclerview);
        mCreatePlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"create player");
                mPresenter.pressedCreatePlayer(mTeamId);
            }
        });


        mAdapter = new TeamPlayersAdapter(mTeamId, mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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
