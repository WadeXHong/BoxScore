package com.bardxhong.boxscore.datastatistic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.gameboxscore.datastatistic.DataStatisticAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatisticFragment extends Fragment implements DataStatisticContract.View{

    private static final String TAG = DataStatisticFragment.class.getSimpleName();

    private DataStatisticContract.Presenter mPresenter;

    private AdaptiveTableLayout mAdaptiveTableLayout;
    private LinkedAdaptiveTableAdapter mLinkedAdaptiveTableAdapter;


    public static DataStatisticFragment newInstance(){
        return new DataStatisticFragment();
    }

    public DataStatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_statistic, container, false);

        mAdaptiveTableLayout = view.findViewById(R.id.fragment_data_statistic_tablelayout);
        mLinkedAdaptiveTableAdapter = new DataStatisticAdapter(getArguments().getString(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, ""));
        mAdaptiveTableLayout.setHeaderFixed(false);
        mAdaptiveTableLayout.setSolidRowHeader(false);

        mAdaptiveTableLayout.setAdapter(mLinkedAdaptiveTableAdapter);



        return view;
    }

    @Override
    public void setPresenter(DataStatisticContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
