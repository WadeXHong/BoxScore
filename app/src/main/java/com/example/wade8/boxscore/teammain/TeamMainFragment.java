package com.example.wade8.boxscore.teammain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamMainFragment extends Fragment implements TeamMainContract.View{
    private static final String TAG = TeamMainFragment.class.getSimpleName();

    private TeamMainContract.Presenter mPresenter;

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



        return view;
    }

    @Override
    public void setPresenter(TeamMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
