package com.bardxhong.boxscore.startgame.playerlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;


public class PlayerListFragment extends Fragment implements PlayerListContract.View{

    private final String TAG = PlayerListFragment.class.getSimpleName();

    private PlayerListContract.Presenter mPresenter;
    private PlayerListAdapter mPlayerListAdapter;

    private RecyclerView mPlayerListRecyclerView;

    public PlayerListFragment() {
        // Required empty public constructor
    }

    public static PlayerListFragment newInstance(){
        return new PlayerListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerListAdapter = new PlayerListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_playerlist, container, false);

        mPlayerListRecyclerView = view.findViewById(R.id.fragment_playerlist_recyclerview);

        mPlayerListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlayerListRecyclerView.setAdapter(mPlayerListAdapter);

        return view;
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {
        gameInfo.setStartingPlayerList(mPlayerListAdapter.getStartingPlayerList());
        gameInfo.setSubstitutePlayerList(mPlayerListAdapter.getSubstitutePlayerList());
    }

    @Override
    public int[] getCheckedInput() {
        return new int[]{mPlayerListAdapter.getStartingPlayerList().size(), mPlayerListAdapter.getSubstitutePlayerList().size()};
    }

    @Override
    public void setDefaultPlayerList(ArrayList<Player> players) {
        mPlayerListAdapter.setPlayerLists(players);
    }

    @Override
    public void setPresenter(PlayerListContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
