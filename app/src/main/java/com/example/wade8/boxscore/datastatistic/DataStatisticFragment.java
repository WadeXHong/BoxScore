package com.example.wade8.boxscore.datastatistic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatisticFragment extends Fragment implements DataStatisticContract.View{

    private static final String TAG = DataStatisticFragment.class.getSimpleName();

    private DataStatisticContract.Presenter mPresenter;


    public static DataStatisticFragment newInstance(){
        return new DataStatisticFragment();
    }

    public DataStatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_statistic, container, false);
    }

    @Override
    public void setPresenter(DataStatisticContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
