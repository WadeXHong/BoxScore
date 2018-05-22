package com.example.wade8.boxscore.historyplayersdata;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPlayersDataFragment extends Fragment implements HistoryPlayersContract.View{

    private static final String TAG = HistoryPlayersDataFragment.class.getSimpleName();

    private HistoryPlayersContract.Presenter mPresenter;



    public static HistoryPlayersDataFragment newInstance(){
        return new  HistoryPlayersDataFragment();
    }

    public HistoryPlayersDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_players_data, container, false);
    }

    @Override
    public void setPresenter(HistoryPlayersContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
