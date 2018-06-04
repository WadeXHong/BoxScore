package com.wadexhong.boxscore.gameboxscore.changeplayer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gameboxscore.changeplayer.changedialog.ChangePlayerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePlayerFragment extends Fragment implements ChangePlayerContract.View{

    private static final String TAG = ChangePlayerFragment.class.getSimpleName();

    private ChangePlayerContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    public static ChangePlayerFragment newInstance(){
        return new ChangePlayerFragment();
    }

    public ChangePlayerFragment() {
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
    public void setAdapter(ChangePlayerAdapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void popInGamePlayerDialog(ChangePlayerDialog dialog) {
        dialog.show(getFragmentManager(),"請選擇下場球員");
    }

    @Override
    public void popOffGamePlayerDialog(ChangePlayerDialog dialog) {
        dialog.show(getFragmentManager(),"請選擇上場球員");
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void setPresenter(ChangePlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
