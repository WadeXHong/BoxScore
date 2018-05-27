package com.example.wade8.boxscore.detailsetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

        mMaxFoulPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulPlus(mMaxFoulTextView.getText().toString());
            }
        });

        mMaxFoulMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.maxFoulMinus(mMaxFoulTextView.getText().toString());
            }
        });

        mQuarterLengthPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthPlus(mQuarterLengthTextView.getText().toString());
            }
        });

        mQuarterLengthMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quarterLengthMinus(mQuarterLengthTextView.getText().toString());
            }
        });

        mTotalQuarterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterPlus(mTotalQuarterTextView.getText().toString());
            }
        });

        mTotalQuarterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.totalQuarterMinus(mTotalQuarterTextView.getText().toString());
            }
        });

        mTimeoutsFirstHalfPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfPlus(mTimeoutsFirstHalfTextView.getText().toString());
            }
        });

        mTimeoutsFirstHalfMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsFirstHalfMinus(mTimeoutsFirstHalfTextView.getText().toString());
            }
        });

        mTimeoutsSecondHalfPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.timeoutsSecondHalfPlus(mTimeoutsSecondHalfTextView.getText().toString());
            }
        });

        mTimeoutsSecondHalfMinus.setOnClickListener(new View.OnClickListener() {
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
    public void showToast(String message) {
        if (message != null){
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {
        gameInfo.setQuarterLength(mQuarterLengthTextView.getText().toString());
        gameInfo.setTotalQuarter(mTotalQuarterTextView.getText().toString());
        gameInfo.setMaxFoul(mMaxFoulTextView.getText().toString());
        gameInfo.setTimeoutFirstHalf(mTimeoutsFirstHalfTextView.getText().toString());
        gameInfo.setTimeoutSecondHalf(mTimeoutsSecondHalfTextView.getText().toString());
    }
}
