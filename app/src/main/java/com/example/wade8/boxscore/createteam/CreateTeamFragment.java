package com.example.wade8.boxscore.createteam;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTeamFragment extends Fragment implements CreateTeamContract.View{

    private static final String TAG = CreateTeamFragment.class.getSimpleName();

    private CreateTeamContract.Presenter mPresenter;

    public static CreateTeamFragment newInstance(){
        return new CreateTeamFragment();
    }
    public CreateTeamFragment() {
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
    public void setPresenter(CreateTeamContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
