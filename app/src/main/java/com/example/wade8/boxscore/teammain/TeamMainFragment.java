package com.example.wade8.boxscore.teammain;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

    private ConstraintLayout mCreateTeamLayout;

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

        mCreateTeamLayout = view.findViewById(R.id.fragment_teammain_createteam_layout);
        mCreateTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressedCreateTeam();
            }
        });



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

    }
}
