package com.example.wade8.boxscore.dialogfragment.datastatistic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.DataStatisticAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatisticDialog extends DialogFragment implements DataStatisticDialogContract.View{

    private static final String TAG = DataStatisticDialog.class.getSimpleName();

    private DataStatisticDialogContract.Presenter mPresenter;

    private FrameLayout mInnerFrameLayout;
    private DataStatisticAdapter mLinkedAdaptiveTableAdapter;
    private AdaptiveTableLayout mAdaptiveTableLayout;


    public static DataStatisticDialog newInstance(){
        return new DataStatisticDialog();
    }

    public DataStatisticDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null){
            return;
        }
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_game_statistic, container, false);
        mInnerFrameLayout = view.findViewById(R.id.dialogfragment_game_statistic_innerlayout);
        mAdaptiveTableLayout = view.findViewById(R.id.dialogfragment_game_statistic_tablelayout);
        mInnerFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"dismiss() executed");
                dismiss();
            }
        });

        mLinkedAdaptiveTableAdapter = new DataStatisticAdapter();
        mAdaptiveTableLayout.setAdapter(mLinkedAdaptiveTableAdapter);
        return view;
    }

    @Override
    public void setPresenter(DataStatisticDialogContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
