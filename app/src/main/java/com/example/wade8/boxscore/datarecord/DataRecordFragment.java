package com.example.wade8.boxscore.datarecord;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment implements DataRecordContract.View{

    private static final String TAG = DataRecordFragment.class.getSimpleName();

    private DataRecordContract.Presenter mPresenter;

    public static DataRecordFragment newInstance(){
        return new DataRecordFragment();
    }

    public DataRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_record, container, false);

        return view;
    }

    @Override
    public void setPresenter(DataRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
