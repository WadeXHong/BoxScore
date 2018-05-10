package com.example.wade8.boxscore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.dialogfragment.PlayerSelectorContract;
import com.example.wade8.boxscore.objects.Player;
import com.example.wade8.boxscore.playeroncourt.changedialog.ChangePlayerDialogContract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class ChangePlayerDialogAdapter extends RecyclerView.Adapter {

    private ChangePlayerDialogContract.Presenter mPresenter;
    private ArrayList<Player> mPlayerArrayList;


    public ChangePlayerDialogAdapter(ChangePlayerDialogContract.Presenter presenter, ArrayList<Player> playerListShowOnDialog) {
        mPresenter = presenter;
        mPlayerArrayList = playerListShowOnDialog;
    }

    public class DialogPlayerViewHolder extends RecyclerView.ViewHolder{

        private TextView mPlayerNumber;
        private TextView mPlayerName;

        public DialogPlayerViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_dialog_player_name);
            mPlayerNumber = itemView.findViewById(R.id.item_dialog_player_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.exchangePlayer(getLayoutPosition());
                }
            });
        }
        private void bind(int position){
            mPlayerName.setText(mPlayerArrayList.get(position).getName());
            mPlayerNumber.setText(mPlayerArrayList.get(position).getNumber());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_player,parent,false);

        return new DialogPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DialogPlayerViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mPlayerArrayList.size();
    }


}
