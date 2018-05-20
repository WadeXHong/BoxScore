package com.example.wade8.boxscore.teammain;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.TeamAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamMainFragment extends Fragment implements TeamMainContract.View{
    private static final String TAG = TeamMainFragment.class.getSimpleName();

    private TeamMainContract.Presenter mPresenter;

    private ConstraintLayout mCreateTeamLayout;
    private RecyclerView mRecyclerView;

    public static TeamMainFragment newInstance(){
        return new TeamMainFragment();
    }

    public TeamMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_main, container, false);

        mRecyclerView = view.findViewById(R.id.fragment_teammain_recyclerview);
        mCreateTeamLayout = view.findViewById(R.id.fragment_teammain_createteam_layout);
        mCreateTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressedCreateTeam();
            }
        });

        mRecyclerView.setAdapter(new TeamAdapter(BoxScore.getTeamDbHelper().getTeamsForAdapter(), mPresenter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mPresenter.refreshToolBar();
        }
    }


    @Override
    public void setPresenter(TeamMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void refreshUI() {
        //library does not support change data dynamically, as read me file I can only create new adapter every times.
        mRecyclerView.setAdapter(new TeamAdapter(BoxScore.getTeamDbHelper().getTeamsForAdapter(), mPresenter));
    }
}
