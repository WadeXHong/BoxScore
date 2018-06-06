package com.wadexhong.boxscore.startgame.detailsetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.GameInfo;


public class DetailSettingFragment extends Fragment implements DetailSettingContract.View {

    private final String TAG = DetailSettingFragment.class.getSimpleName();

    private DetailSettingContract.Presenter mPresenter;

    private Button mQuarterLengthPlusButton;
    private Button mQuarterLengthMinusButton;
    private Button mTotalQuarterPlusButton;
    private Button mTotalQuarterMinusButton;
    private Button mMaxFoulPlusButton;
    private Button mMaxFoulMinusButton;
    private Button mTimeoutsFirstHalfPlusButton;
    private Button mTimeoutsFirstHalfMinusButton;
    private Button mTimeoutsSecondHalfPlusButton;
    private Button mTimeoutsSecondHalfMinusButton;

    private TextView mQuarterLengthTextView;
    private TextView mTotalQuarterTextView;
    private TextView mMaxFoulTextView;
    private TextView mTimeoutsFirstHalfTextView;
    private TextView mTimeoutsSecondHalfTextView;

    private String mQuaterLength;
    private String mTotalQuarter;
    private String mMaxFoul;
    private String mTimeoutsFirstHalf;
    private String mTimeoutsSecondHalf;

    public DetailSettingFragment() {
        // Required empty public constructor
    }

    public static DetailSettingFragment newInstance() {
        return new DetailSettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_setting, container, false);

        mQuarterLengthPlusButton = view.findViewById(R.id.fragment_detailsetting_quarterlength_plus);
        mQuarterLengthMinusButton = view.findViewById(R.id.fragment_detailsetting_quarterlength_minus);
        mTotalQuarterPlusButton = view.findViewById(R.id.fragment_detailsetting_totalquarter_plus);
        mTotalQuarterMinusButton = view.findViewById(R.id.fragment_detailsetting_totalquarter_minus);
        mMaxFoulPlusButton = view.findViewById(R.id.fragment_detailsetting_maxfoul_plus);
        mMaxFoulMinusButton = view.findViewById(R.id.fragment_detailsetting_maxfoul_minus);
        mTimeoutsFirstHalfPlusButton = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_plus);
        mTimeoutsFirstHalfMinusButton = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_minus);
        mTimeoutsSecondHalfPlusButton = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_plus);
        mTimeoutsSecondHalfMinusButton = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_minus);

        mQuarterLengthTextView = view.findViewById(R.id.fragment_detailsetting_quarterlength_textview);
        mTotalQuarterTextView = view.findViewById(R.id.fragment_detailsetting_totalquarter_edittext);
        mMaxFoulTextView = view.findViewById(R.id.fragment_detailsetting_maxfoul_edittext);
        mTimeoutsFirstHalfTextView = view.findViewById(R.id.fragment_detailsetting_timeouts_firsthalf_edittext);
        mTimeoutsSecondHalfTextView = view.findViewById(R.id.fragment_detailsetting_timeouts_secondhalf_edittext);

        mQuaterLength = mQuarterLengthTextView.getText().toString();
        mTotalQuarter = mTotalQuarterTextView.getText().toString();
        mMaxFoul = mMaxFoulTextView.getText().toString();
        mTimeoutsFirstHalf = mTimeoutsFirstHalfTextView.getText().toString();
        mTimeoutsSecondHalf = mTimeoutsSecondHalfTextView.getText().toString();

        setOnClickOnButton();

        return view;
    }

    private void setOnClickOnButton() {

        mMaxFoulPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulPlus(mMaxFoulTextView.getText().toString());
            }
        });

        mMaxFoulMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulMinus(mMaxFoulTextView.getText().toString());
            }
        });

        mQuarterLengthPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthPlus(mQuarterLengthTextView.getText().toString());
            }
        });

        mQuarterLengthMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthMinus(mQuarterLengthTextView.getText().toString());
            }
        });

        mTotalQuarterPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterPlus(mTotalQuarterTextView.getText().toString());
            }
        });

        mTotalQuarterMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterMinus(mTotalQuarterTextView.getText().toString());
            }
        });

        mTimeoutsFirstHalfPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfPlus(mTimeoutsFirstHalfTextView.getText().toString());
            }
        });

        mTimeoutsFirstHalfMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfMinus(mTimeoutsFirstHalfTextView.getText().toString());
            }
        });

        mTimeoutsSecondHalfPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsSecondHalfPlus(mTimeoutsSecondHalfTextView.getText().toString());
            }
        });

        mTimeoutsSecondHalfMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsSecondHalfMinus(mTimeoutsSecondHalfTextView.getText().toString());
            }
        });
    }

    @Override
    public void setPresenter(DetailSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMaxFoulInEditText(String s) {
        mMaxFoulTextView.setText(s);
    }

    @Override
    public void updateQuarterLengthEditText(String s) {
        mQuarterLengthTextView.setText(s);
    }

    @Override
    public void updateTotalQuarterEditText(String s) {
        mTotalQuarterTextView.setText(s);
    }

    @Override
    public void updateTimeoutsFirstHalfEditText(String s) {
        mTimeoutsFirstHalfTextView.setText(s);
    }

    @Override
    public void updateTimeoutsSecondHalfEditText(String s) {
        mTimeoutsSecondHalfTextView.setText(s);
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {
        gameInfo.setQuarterLength(mQuarterLengthTextView.getText().toString());
        gameInfo.setTotalQuarter(mTotalQuarterTextView.getText().toString());
        gameInfo.setMaxFoul(mMaxFoulTextView.getText().toString());
        gameInfo.setTimeoutFirstHalf(mTimeoutsFirstHalfTextView.getText().toString());
        gameInfo.setTimeoutSecondHalf(mTimeoutsSecondHalfTextView.getText().toString());
    }

    @Override
    public void showToast(String message) {
        if (message != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
