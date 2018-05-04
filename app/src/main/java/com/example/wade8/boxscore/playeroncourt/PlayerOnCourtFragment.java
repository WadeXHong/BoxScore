package com.example.wade8.boxscore.playeroncourt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerOnCourtFragment extends Fragment implements PlayerOnCourtContract.View{

    private static final String TAG = PlayerOnCourtFragment.class.getSimpleName();

    private PlayerOnCourtContract.Presenter mPresenter;

    public static PlayerOnCourtFragment newInstance(){
        return new PlayerOnCourtFragment();
    }

    public PlayerOnCourtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_on_court, container, false);
    }

    @Override
    public void setPresenter(PlayerOnCourtContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
