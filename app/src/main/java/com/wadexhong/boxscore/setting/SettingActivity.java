package com.wadexhong.boxscore.setting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;

public class SettingActivity extends AppCompatActivity implements SettingContract.View {

    private final String TAG = SettingActivity.class.getSimpleName();

    private SettingContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private SettingAdapter mSettingAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        setBrightness(BoxScore.sBrightness);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mRecyclerView = findViewById(R.id.activity_setting_recyclerview);

        mPresenter = new SettingPresenter(this);

        mSettingAdapter = new SettingAdapter(mPresenter, getIntent().getBooleanExtra(Constants.ExtraNames.SETTING_BOOLEAN_IS_SHOW_LOGOUT, false));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSettingAdapter);

    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
        layoutParams.screenBrightness = brightness; // Set Value
        getWindow().setAttributes(layoutParams); // Set params
    }

    @Override
    public void finishActivity() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}