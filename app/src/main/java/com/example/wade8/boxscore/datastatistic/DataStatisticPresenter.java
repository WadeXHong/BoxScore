package com.example.wade8.boxscore.datastatistic;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataStatisticPresenter implements DataStatisticContract.Presenter{

    private static final String TAG = DataStatisticPresenter.class.getSimpleName();

    private final DataStatisticContract.View mDataStatisticView;


    public DataStatisticPresenter(DataStatisticContract.View dataStatisticView) {
        mDataStatisticView = dataStatisticView;
    }

    @Override
    public void start() {

    }
}
