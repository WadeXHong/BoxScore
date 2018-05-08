package com.example.wade8.boxscore.dialogfragment.datastatistic;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataStatisticDialogPresenter implements DataStatisticDialogContract.Presenter{

    private static final String TAG = DataStatisticDialogPresenter.class.getSimpleName();

    private final DataStatisticDialogContract.View mDataStatisticView;


    public DataStatisticDialogPresenter(DataStatisticDialogContract.View dataStatisticView) {
        mDataStatisticView = dataStatisticView;
        mDataStatisticView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
