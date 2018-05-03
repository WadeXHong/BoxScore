package com.example.wade8.boxscore.detailsetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.GameInfo;


public class DetailSettingFragment extends Fragment implements DetailSettingContract.View {

    private static final String TAG = DetailSettingFragment.class.getSimpleName();

    private DetailSettingContract.Presenter mPresenter;

    private Button mQuarterLengthPlus;
    private Button mQuarterLengthMinus;
    private Button mTotalQuarterPlus;
    private Button mTotalQuarterMinus;
    private Button mMaxFoulPlus;
    private Button mMaxFoulMinus;
    private Button mTimeoutsFirstHalfPlus;
    private Button mTimeoutsFirstHalfMinus;
    private Button mTimeoutsSecondHalfPlus;
    private Button mTimeoutsSecondHalfMinus;

    private EditText mQuarterLengthEditText;
    private EditText mTotalQuarterEditText;
    private EditText mMaxFoulEditText;
    private EditText mTimeoutsFirstHalfEditText;
    private EditText mTimeoutsSecondHalfEditText;

    private String mQuaterLength;
    private String mTotalQuarter;
    private String mMaxFoul;
    private String mTimeoutsFirstHalf;
    private String mTimeoutsSecondHalf;

    public DetailSettingFragment() {
        // Required empty public constructor
    }

    public static DetailSettingFragment newInstance(){
        return new DetailSettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_setting, container, false);

        mQuarterLengthPlus = view.findViewById(R.id.fragment_detailsetting_quarterlength_plus);
        mQuarterLengthMinus = view.findViewById(R.id.fragment_detailsetting_quarterlength_minus);
        mTotalQuarterPlus = view.findViewById(R.id.fragment_detailsetting_totalquarter_plus);
        mTotalQuarterMinus = view.findViewById(R.id.fragment_detailsetting_totalquarter_minus);
        mMaxFoulPlus = view.findViewById(R.id.fragment_detailsetting_maxfoul_plus);
        mMaxFoulMinus = view.findViewById(R.id.fragment_detailsetting_maxfoul_minus);
        mTimeoutsFirstHalfPlus = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_plus);
        mTimeoutsFirstHalfMinus = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_minus);
        mTimeoutsSecondHalfPlus = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_plus);
        mTimeoutsSecondHalfMinus = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_minus);

        mQuarterLengthEditText = view.findViewById(R.id.fragment_detailsetting_quarterlength_edittext);
        mTotalQuarterEditText = view.findViewById(R.id.fragment_detailsetting_totalquarter_edittext);
        mMaxFoulEditText = view.findViewById(R.id.fragment_detailsetting_maxfoul_edittext);
        mTimeoutsFirstHalfEditText = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_edittext);
        mTimeoutsSecondHalfEditText = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_edittext);

        mQuaterLength = mQuarterLengthEditText.getText().toString();
        mTotalQuarter = mTotalQuarterEditText.getText().toString();
        mMaxFoul = mMaxFoulEditText.getText().toString();
        mTimeoutsFirstHalf = mTimeoutsFirstHalfEditText.getText().toString();
        mTimeoutsSecondHalf = mTimeoutsSecondHalfEditText.getText().toString();

        setOnClickOnButton();

        return view;
    }

    private void setOnClickOnButton() {

        mMaxFoulPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulPlus(mMaxFoulEditText.getText().toString());
            }
        });

        mMaxFoulMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulMinus(mMaxFoulEditText.getText().toString());
            }
        });

        mQuarterLengthPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthPlus(mQuarterLengthEditText.getText().toString());
            }
        });

        mQuarterLengthMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthMinus(mQuarterLengthEditText.getText().toString());
            }
        });

        mTotalQuarterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterPlus(mTotalQuarterEditText.getText().toString());
            }
        });

        mTotalQuarterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterMinus(mTotalQuarterEditText.getText().toString());
            }
        });

        mTimeoutsFirstHalfPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfPlus(mTimeoutsFirstHalfEditText.getText().toString());
            }
        });

        mTimeoutsFirstHalfMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfMinus(mTimeoutsFirstHalfEditText.getText().toString());
            }
        });

        mTimeoutsSecondHalfPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsSecondHalfPlus(mTimeoutsSecondHalfEditText.getText().toString());
            }
        });

        mTimeoutsSecondHalfMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsSecondHalfMinus(mTimeoutsSecondHalfEditText.getText().toString());
            }
        });
    }

    @Override
    public void setPresenter(DetailSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMaxFoulInEditText(String s) {
        mMaxFoulEditText.setText(s);
    }

    @Override
    public void updateQuarterLengthEditText(String s) {
        mQuarterLengthEditText.setText(s);
    }

    @Override
    public void updateTotalQuarterEditText(String s) {
        mTotalQuarterEditText.setText(s);
    }

    @Override
    public void updateTimeoutsFirstHalfEditText(String s) {
        mTimeoutsFirstHalfEditText.setText(s);
    }

    @Override
    public void updateTimeoutsSecondHalfEditText(String s) {
        mTimeoutsSecondHalfEditText.setText(s);
    }

    @Override
    public void showToast(String message) {
        if (message != null){
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {
        gameInfo.setQuarterLength(mQuarterLengthEditText.getText().toString());
        gameInfo.setTotalQuarter(mTotalQuarterEditText.getText().toString());
        gameInfo.setMaxFoul(mMaxFoulEditText.getText().toString());
        gameInfo.setTimeoutFirstHalf(mTimeoutsFirstHalfEditText.getText().toString());
        gameInfo.setTimeoutSecondHalf(mTimeoutsSecondHalfEditText.getText().toString());
    }
}
