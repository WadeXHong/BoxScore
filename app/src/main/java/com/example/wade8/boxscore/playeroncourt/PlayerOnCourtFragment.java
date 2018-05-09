package com.example.wade8.boxscore.playeroncourt;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.PlayerOnCourtAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerOnCourtFragment extends Fragment implements PlayerOnCourtContract.View{

    private static final String TAG = PlayerOnCourtFragment.class.getSimpleName();

    private PlayerOnCourtContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    public static PlayerOnCourtFragment newInstance(){
        return new PlayerOnCourtFragment();
    }

    public PlayerOnCourtFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_on_court, container, false);

        mRecyclerView = view.findViewById(R.id.fragment_playeroncourt_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.start();

        return view;
    }

    @Override
    public void setAdapter(PlayerOnCourtAdapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setPresenter(PlayerOnCourtContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
