package com.wadexhong.boxscore.gameboxscore.datarecord.playerselect;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.gameboxscore.datarecord.DataRecordFragment;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectDialog extends android.support.v4.app.DialogFragment implements PlayerSelectContract.View {

    private static final String TAG = PlayerSelectDialog.class.getSimpleName();
    private static final String BUNDLE_KEY = "TYPE";

    private PlayerSelectContract.Presenter mPresenter;

    private ConstraintLayout mParentLayout;
    private LinearLayout mInnerLayout;
    private TextView mTitleTextView;
    private TextView mCancelTextView;
    private RecyclerView mPlayerRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int mType;

    public static PlayerSelectDialog newInstance(int type) {

        PlayerSelectDialog dialog = new PlayerSelectDialog();
        Bundle bdl = new Bundle(1);
        bdl.putInt(BUNDLE_KEY, type);
        dialog.setArguments(bdl);

        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = getArguments().getInt(BUNDLE_KEY, -1);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        mPresenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogfragment_select_player, container, false);

        mParentLayout = view.findViewById(R.id.dialogfragment_playerselect_parentlayout);
        mInnerLayout = view.findViewById(R.id.dialogfragment_playerselect_innerlayout);
        mTitleTextView = view.findViewById(R.id.dialogfragment_playerselect_title);
        mCancelTextView = view.findViewById(R.id.dialogfragment_playerselect_cancel);
        mPlayerRecyclerView = view.findViewById(R.id.dialogfragment_playerselect_recyclerview);

        mTitleTextView.setText(getTag());

        mAdapter = new PlayerSelectAdapter(mPresenter, mType);
        mPlayerRecyclerView.setAdapter(mAdapter);
        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel pressed");
                dismiss();
            }
        });
        mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Outside pressed");
                dismiss();
            }
        });
        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "InnerLayout pressed");

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView");
        getTargetFragment().onActivityResult(DataRecordFragment.REQUEST_PLAYER_SELECT_DIALOG, Activity.RESULT_OK, new Intent());
    }

    @Override
    public void dismiss() {
        super.dismiss();

        Log.d(TAG, "dismiss");
//        getTargetFragment().onActivityResult(DataRecordFragment.REQUEST_PLAYER_SELECT_DIALOG, Activity.RESULT_OK,new Intent());
    }

    @Override
    public void setPresenter(PlayerSelectContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
