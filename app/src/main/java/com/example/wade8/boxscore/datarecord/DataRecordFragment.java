package com.example.wade8.boxscore.datarecord;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment implements DataRecordContract.View{

    private static final String TAG = DataRecordFragment.class.getSimpleName();
    public static final int REQUEST_PLAYERSELECTDIALOG = 0;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PLAYERSELECTDIALOG){
            enableAllButtons(true);
        }

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
                mPresenter.pressTwoPoint();
            }
        });
        mThreePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressThreePoint();
            }
        });
        mFreeThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressFreeThrow();
            }
        });
        mAssist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressAssist();
            }
        });
        mOffensiveRebound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressOffensiveRebound();
            }
        });
        mSteal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressSteal();
            }
        });
        mBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressBlock();
            }
        });
        mFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressFoul();
            }
        });
        mTurnover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressTurnover();
            }
        });
        mDefensiveRebound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pressDefensiveRebound();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void setPresenter(DataRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void popPlayerSelectDialog(PlayerSelectDialog dialog, int stringId) {
        dialog.setTargetFragment(this,REQUEST_PLAYERSELECTDIALOG);
        dialog.show(getFragmentManager(),getResources().getString(stringId));
    }

    @Override
    public void popIsShotMadeDialog(final int type) {
        new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.isShotMade)
                  .setItems(new String[]{getString(R.string.yes), getString(R.string.no)}, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          switch (which){
                              case 0:
                                  Log.d(TAG,"命中");
                                  mPresenter.pressShotMade(type+1);
                                  break;
                              case 1:
                                  Log.d(TAG,"失手");
                                  mPresenter.pressShotMissed(type+2);
                                  break;
                          }
                      }
                  }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG,"取消");
                enableAllButtons(true);
            }
        })
                  .create().show();
    }

    @Override
    public void enableAllButtons(boolean setEnableOrNot) {
        mTwoPoint.setEnabled(setEnableOrNot);
        mThreePoint.setEnabled(setEnableOrNot);
        mFreeThrow.setEnabled(setEnableOrNot);
        mAssist.setEnabled(setEnableOrNot);
        mOffensiveRebound.setEnabled(setEnableOrNot);
        mSteal.setEnabled(setEnableOrNot);
        mBlock.setEnabled(setEnableOrNot);
        mFoul.setEnabled(setEnableOrNot);
        mTurnover.setEnabled(setEnableOrNot);
        mDefensiveRebound.setEnabled(setEnableOrNot);
        Log.d(TAG,"All record buttons enable or not : " + setEnableOrNot);
    }


}
