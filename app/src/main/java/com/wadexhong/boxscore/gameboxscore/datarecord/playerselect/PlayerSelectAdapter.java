package com.wadexhong.boxscore.gameboxscore.datarecord.playerselect;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectAdapter extends RecyclerView.Adapter {

    private PlayerSelectContract.Presenter mPresenter;
    private ArrayList<Player> mPlayerArrayList;
    private int mType;


    public PlayerSelectAdapter(PlayerSelectContract.Presenter presenter, int type) {
        mPresenter = presenter;
        mPlayerArrayList = deepClonePlayerList(mPresenter.getPlayerOnCourt());
        mType = type;
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
                    mPresenter.editDataInDB(getLayoutPosition(),mType);
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

    private ArrayList<Player> deepClonePlayerList(ArrayList<Player> originList){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ArrayList<Player> retunPlayerList = originList;
        byte[] byteData = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(originList);
            oos.flush();
            oos.close();
            bos.close();
            byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            retunPlayerList = (ArrayList<Player>)new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException mE) {
            mE.printStackTrace();
        }
        return retunPlayerList;
    }
}
