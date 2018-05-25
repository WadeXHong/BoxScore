package com.example.wade8.boxscore.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.SettingAdapter;

public class SettingActivity extends AppCompatActivity implements SettingContract.View{

    private static final String TAG = SettingActivity.class.getSimpleName();

    private SettingContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private SettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mRecyclerView = findViewById(R.id.activity_setting_recyclerview);

        mPresenter = new SettingPresenter(this);

        mAdapter = new SettingAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
