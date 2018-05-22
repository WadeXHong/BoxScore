package com.example.wade8.boxscore.historyteamdata;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTeamDataFragment extends Fragment implements HistoryTeamDataContract.View{

    private static final String TAG = HistoryTeamDataFragment.class.getSimpleName();

    private HistoryTeamDataContract.Presenter mPresenter;

    public static HistoryTeamDataFragment newInstance(){
        return new HistoryTeamDataFragment();
    }

    public HistoryTeamDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_team_data, container, false);
    }

    @Override
    public void setPresenter(HistoryTeamDataContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
