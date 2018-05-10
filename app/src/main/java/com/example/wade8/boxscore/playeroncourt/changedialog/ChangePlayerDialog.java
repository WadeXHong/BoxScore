package com.example.wade8.boxscore.playeroncourt.changedialog;


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

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.ChangePlayerAdapter;
import com.example.wade8.boxscore.adapter.ChangePlayerDialogAdapter;
import com.example.wade8.boxscore.adapter.DialogPlayerAdapter;
import com.example.wade8.boxscore.datarecord.DataRecordFragment;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class ChangePlayerDialog extends DialogFragment implements ChangePlayerDialogContract.View {

    private static final String TAG = ChangePlayerDialog.class.getSimpleName();
    private static final String BUNDLE_KEY = "Position";
    private ChangePlayerDialogContract.Presenter mPresenter;

    private ConstraintLayout mParentLayout;
    private LinearLayout mInnerLayout;
    private TextView mTitle;
    private TextView mCancel;
    private RecyclerView mPlayerRecyclerView;

    private int mPosition;

    public static ChangePlayerDialog newInstance(int position){
        ChangePlayerDialog dialog = new ChangePlayerDialog();
        Bundle bdl = new Bundle(1);
        bdl.putInt(BUNDLE_KEY, position);
        dialog.setArguments(bdl);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(BUNDLE_KEY,-1);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        mPresenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogfragment_select_player,container,false);

        mParentLayout = view.findViewById(R.id.dialogfragment_playerselect_parentlayout);
        mInnerLayout = view.findViewById(R.id.dialogfragment_playerselect_innerlayout);
        mTitle = view.findViewById(R.id.dialogfragment_playerselect_title);
        mCancel = view.findViewById(R.id.dialogfragment_playerselect_cancel);
        mPlayerRecyclerView = view.findViewById(R.id.dialogfragment_playerselect_recyclerview);
        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPresenter.initAdapter();

        mTitle.setText(getTag());


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cancel pressed");
                dismiss();
            }
        });
        mParentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Outside pressed");
                dismiss();
            }
        });
        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"InnerLayout pressed");

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null){
            return;
        }

        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG,"dismiss");
    }

    @Override
    public void setRecyclerView(ChangePlayerDialogAdapter mAdapter) {
        mPlayerRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setPresenter(ChangePlayerDialogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
