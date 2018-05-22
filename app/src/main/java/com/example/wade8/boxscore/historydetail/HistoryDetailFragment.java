package com.example.wade8.boxscore.historydetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;


public class HistoryDetailFragment extends Fragment implements HistoryDetailContract.View{

    private static final String TAG = HistoryDetailFragment.class.getSimpleName();

    private HistoryDetailContract.Presenter mPresenter;

    public HistoryDetailFragment() {
    }

    public static HistoryDetailFragment newInstance() {
        return new HistoryDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
    }

    @Override
    public void setPresenter(HistoryDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
