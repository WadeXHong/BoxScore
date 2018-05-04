package com.example.wade8.boxscore.datarecord;

/**
 * Created by wade8 on 2018/5/3.
 */

public class DataRecordPresenter implements DataRecordContract.Presenter{

    private static final String TAG = DataRecordPresenter.class.getSimpleName();

    private final DataRecordContract.View mDataRecordView;

    public DataRecordPresenter(DataRecordContract.View dataRecordView) {
        mDataRecordView = dataRecordView;
        mDataRecordView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
