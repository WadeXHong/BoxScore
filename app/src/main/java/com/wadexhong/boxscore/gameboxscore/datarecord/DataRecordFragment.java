package com.wadexhong.boxscore.gameboxscore.datarecord;


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

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gameboxscore.datarecord.playerselect.PlayerSelectDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment implements DataRecordContract.View {

    private final String TAG = DataRecordFragment.class.getSimpleName();
    public static final int REQUEST_PLAYER_SELECT_DIALOG = 0;

    private DataRecordContract.Presenter mPresenter;

    private Button mTwoPointButton;
    private Button mThreePointButton;
    private Button mFreeThrowButton;
    private Button mAssistButton;
    private Button mOffensiveReboundButton;
    private Button mStealButton;
    private Button mBlockButton;
    private Button mFoulButton;
    private Button mTurnoverButton;
    private Button mDefensiveReboundButton;

    public static DataRecordFragment newInstance() {
        return new DataRecordFragment();
    }

    public DataRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PLAYER_SELECT_DIALOG) {
            enableAllButtons(true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_record, container, false);

        mTwoPointButton = view.findViewById(R.id.datarecord_twopoint);
        mThreePointButton = view.findViewById(R.id.datarecord_threepoint);
        mFreeThrowButton = view.findViewById(R.id.datarecord_freethrow);
        mAssistButton = view.findViewById(R.id.datarecord_assist);
        mOffensiveReboundButton = view.findViewById(R.id.datarecord_offensiverebound);
        mStealButton = view.findViewById(R.id.datarecord_steal);
        mBlockButton = view.findViewById(R.id.datarecord_block);
        mFoulButton = view.findViewById(R.id.datarecord_foul);
        mTurnoverButton = view.findViewById(R.id.datarecord_turnover);
        mDefensiveReboundButton = view.findViewById(R.id.datarecord_defensiverebound);

        setOnClick();

        return view;
    }

    private void setOnClick() {
        mTwoPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressTwoPoint();
            }
        });
        mThreePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressThreePoint();
            }
        });
        mFreeThrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressFreeThrow();
            }
        });
        mAssistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressAssist();
            }
        });
        mOffensiveReboundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressOffensiveRebound();
            }
        });
        mStealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressSteal();
            }
        });
        mBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressBlock();
            }
        });
        mFoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressFoul();
            }
        });
        mTurnoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressTurnover();
            }
        });
        mDefensiveReboundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxScore.vibrate();
                mPresenter.pressDefensiveRebound();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.start();
    }

    @Override
    public void popPlayerSelectDialog(PlayerSelectDialog dialog, int stringId) {

        dialog.setTargetFragment(this, REQUEST_PLAYER_SELECT_DIALOG);
        dialog.show(getFragmentManager(), getResources().getString(stringId));

    }

    @Override
    public void popIsShotMadeDialog(final int type) {

        new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.isShotMade)
                  .setItems(new String[]{getString(R.string.yes), getString(R.string.no)}, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                          switch (which) {

                              case 0:
                                  mPresenter.pressShotMade(type + Constants.RecordDataType.SHIFT_SHOT_MADE);
                                  break;

                              case 1:
                                  mPresenter.pressShotMissed(type + Constants.RecordDataType.SHIFT_SHOT_MISSED);
                                  break;
                          }
                      }
                  })
                  .setOnCancelListener(new DialogInterface.OnCancelListener() {
                      @Override
                      public void onCancel(DialogInterface dialog) {

                          enableAllButtons(true);
                      }
                  })
                  .create().show();
    }

    @Override
    public void enableAllButtons(boolean setEnableOrNot) {

        mTwoPointButton.setEnabled(setEnableOrNot);
        mThreePointButton.setEnabled(setEnableOrNot);
        mFreeThrowButton.setEnabled(setEnableOrNot);
        mAssistButton.setEnabled(setEnableOrNot);
        mOffensiveReboundButton.setEnabled(setEnableOrNot);
        mStealButton.setEnabled(setEnableOrNot);
        mBlockButton.setEnabled(setEnableOrNot);
        mFoulButton.setEnabled(setEnableOrNot);
        mTurnoverButton.setEnabled(setEnableOrNot);
        mDefensiveReboundButton.setEnabled(setEnableOrNot);

        Log.d(TAG, "All record buttons enable or not : " + setEnableOrNot);
    }

    @Override
    public void setPresenter(DataRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
