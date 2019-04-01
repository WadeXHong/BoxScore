package com.bardxhong.boxscore.gameboxscore.changeplayer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.gameboxscore.changeplayer.changedialog.ChangePlayerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePlayerFragment extends Fragment implements ChangePlayerContract.View {

    private final String TAG = ChangePlayerFragment.class.getSimpleName();

    private ChangePlayerContract.Presenter mPresenter;

    private RecyclerView mPlayerListRecyclerView;

    public static ChangePlayerFragment newInstance() {
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

        View view = inflater.inflate(R.layout.fragment_change_player, container, false);

        mPlayerListRecyclerView = view.findViewById(R.id.fragment_change_player_recyclerview);
        mPlayerListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPresenter.start();

        return view;
    }

    @Override
    public void setAdapter(ChangePlayerAdapter adapter) {
        mPlayerListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void popInGamePlayerDialog(ChangePlayerDialog dialog) {
        dialog.show(getFragmentManager(), getString(R.string.change_player_off_game));
    }

    @Override
    public void popOffGamePlayerDialog(ChangePlayerDialog dialog) {
        dialog.show(getFragmentManager(), getString(R.string.change_player_in_game));
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void setPresenter(ChangePlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
