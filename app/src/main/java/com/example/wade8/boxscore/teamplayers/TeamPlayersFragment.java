package com.example.wade8.boxscore.teamplayers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamPlayersFragment extends Fragment implements TeamPlayersContract.View{

    private static final String TAG = TeamPlayersFragment.class.getSimpleName();

    private TeamPlayersContract.Presenter mPresenter;

    public static TeamPlayersFragment newInstance(){
        return new TeamPlayersFragment();
    }
    public TeamPlayersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);

        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(TeamPlayersContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
