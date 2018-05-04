package com.example.wade8.boxscore.dialogfragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.adapter.DialogPlayerAdapter;
import com.example.wade8.boxscore.adapter.PlayerListAdapter;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectDialog extends android.support.v4.app.DialogFragment implements PlayerSelecterContract.View {

    private static final String TAG = PlayerSelectDialog.class.getSimpleName();

    private PlayerSelecterContract.Presenter mPresenter;

    private RecyclerView mPlayerRecyclerView;
    private RecyclerView.Adapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogfragment_select_player,container,false);
        mPlayerRecyclerView = view.findViewById(R.id.dialogfragment_playerselect_recyclerview);
        //FAKEFAKE
        ArrayList<Player> mList = new ArrayList<>();
        mList.add(new Player("87","洪偉軒"));
        //FAKEFAKE
        mAdapter = new DialogPlayerAdapter(mPresenter,mList);
        mPlayerRecyclerView.setAdapter(mAdapter);
        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
    public void setPresenter(PlayerSelecterContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
