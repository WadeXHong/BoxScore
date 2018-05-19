package com.example.wade8.boxscore.createteam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wade8.boxscore.R;

/**
 * Created by wade8 on 2018/5/18.
 */

public class CreateTeamDialogFragment extends DialogFragment implements CreateTeamContract.View{

    private static final String TAG = CreateTeamDialogFragment.class.getSimpleName();

    private CreateTeamContract.Presenter mPresenter;

    private ConstraintLayout mParentLayout;
    private LinearLayout mInnerLayout;
    private EditText mEditText;
    private TextView mCancel;
    private TextView mConfirm;

    public static CreateTeamDialogFragment newInstance(){
        return new CreateTeamDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog);
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogfragment_create_team, container, false);

        mParentLayout = view.findViewById(R.id.dialogfragment_createteam_parentlayout);
        mInnerLayout = view.findViewById(R.id.dialogfragment_createteam_innerlayout);
        mEditText = view.findViewById(R.id.dialogfragment_createteam_teamname);
        mCancel = view.findViewById(R.id.dialogfragment_createteam_cancel);
        mConfirm = view.findViewById(R.id.dialogfragment_createteam_confirm);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cancel pressed");
                dismiss();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Confirm pressed");
                mPresenter.pressedConfirm(mEditText.getText().toString());
            }
        });
        mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Outside pressed");
                dismiss();
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().equals("")){
                    mConfirm.setEnabled(true);
                    mEditText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.shape_edittext_gamename,null));
                }else {
                    mConfirm.setEnabled(false);
                    mEditText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.shape_edittext_gamename_illegal,null));
                }
            }
        });

        mPresenter.start();

        return view;
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
    public void setPresenter(CreateTeamContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }

    @Override
    public void showIllegalHint() { //TODO show error message
        mEditText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.shape_edittext_gamename_illegal,null));
    }
}
