package com.bardxhong.boxscore.gameboxscore.datastatistic;


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
import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataStatisticDialog extends DialogFragment implements DataStatisticDialogContract.View {

    private final String TAG = DataStatisticDialog.class.getSimpleName();

    private DataStatisticDialogContract.Presenter mPresenter;

    private FrameLayout mInnerFrameLayout;
    private DataStatisticAdapter mLinkedAdaptiveTableAdapter;
    private AdaptiveTableLayout mAdaptiveTableLayout;


    public static DataStatisticDialog newInstance(String gameId) {

        DataStatisticDialog dialog = new DataStatisticDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, gameId);
        dialog.setArguments(bundle);

        return dialog;
    }

    public DataStatisticDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
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
                Log.d(TAG, "dismissAllowingStateLoss() executed");
                dismiss();
            }
        });

        mLinkedAdaptiveTableAdapter = new DataStatisticAdapter(getArguments().getString(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID, ""));
        mAdaptiveTableLayout.setAdapter(mLinkedAdaptiveTableAdapter);
        return view;
    }

    @Override
    public void setPresenter(DataStatisticDialogContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
