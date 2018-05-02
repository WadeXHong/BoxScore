package com.example.wade8.boxscore.playerlist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.PlayerListAdapter;


public class PlayerListFragment extends Fragment implements PlayerListContract.View{

    private static final String TAG = PlayerListFragment.class.getSimpleName();

    private PlayerListContract.Presenter mPresenter;
    private PlayerListAdapter mAdapter;

    private RecyclerView mRecyclerView;

    public PlayerListFragment() {
        // Required empty public constructor
    }

    public static PlayerListFragment newInstance(){
        return new PlayerListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PlayerListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_playerlist, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_playerlist_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setPresenter(PlayerListContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
