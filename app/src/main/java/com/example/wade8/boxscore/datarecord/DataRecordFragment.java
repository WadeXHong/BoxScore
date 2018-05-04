package com.example.wade8.boxscore.datarecord;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment implements DataRecordContract.View{

    private static final String TAG = DataRecordFragment.class.getSimpleName();

    private DataRecordContract.Presenter mPresenter;

    private Button mTwoPoint;
    private Button mThreePoint;
    private Button mFreeThrow;
    private Button mAssist;
    private Button mOffensiveRebound;
    private Button mSteal;
    private Button mBlock;
    private Button mFoul;
    private Button mTurnover;
    private Button mDefensiveRebound;


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

        mTwoPoint = view.findViewById(R.id.datarecord_twopoint);
        mThreePoint = view.findViewById(R.id.datarecord_threepoint);
        mFreeThrow = view.findViewById(R.id.datarecord_freethrow);
        mAssist = view.findViewById(R.id.datarecord_assist);
        mOffensiveRebound = view.findViewById(R.id.datarecord_offensiverebound);
        mSteal = view.findViewById(R.id.datarecord_steal);
        mBlock = view.findViewById(R.id.datarecord_block);
        mFoul = view.findViewById(R.id.datarecord_foul);
        mTurnover = view.findViewById(R.id.datarecord_turnover);
        mDefensiveRebound = view.findViewById(R.id.datarecord_defensiverebound);

        mTwoPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressTwoPoint();
                PlayerSelectDialog dialog = new PlayerSelectDialog();
                PlayerSelectPresenter dialogPresenter = new PlayerSelectPresenter(dialog);
                dialog.show(getFragmentManager(),"TwoPoint");
            }
        });
        mThreePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressThreePoint();
            }
        });
        mFreeThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressFreeThrow();
            }
        });
        mAssist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressAssist();
            }
        });
        mOffensiveRebound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressOffensiveRebound();
            }
        });
        mSteal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressSteal();
            }
        });
        mBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressBlock();
            }
        });
        mFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressFoul();
            }
        });
        mTurnover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressTurnover();
            }
        });
        mDefensiveRebound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.PressDefensiveRebound();
            }
        });

        return view;
    }

    @Override
    public void setPresenter(DataRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
