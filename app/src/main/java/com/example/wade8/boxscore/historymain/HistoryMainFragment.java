package com.example.wade8.boxscore.historymain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryMainFragment extends Fragment implements HistoryMainContract.View{

    private static final String TAG = HistoryMainFragment.class.getSimpleName();

    private HistoryMainContract.Presenter mPresenter;

    public static HistoryMainFragment newInstance(){
        return new HistoryMainFragment();
    }

    public HistoryMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_main, container, false);
    }

    @Override
    public void setPresenter(HistoryMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
