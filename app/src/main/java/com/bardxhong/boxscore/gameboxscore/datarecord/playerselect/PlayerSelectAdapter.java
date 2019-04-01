package com.bardxhong.boxscore.gameboxscore.datarecord.playerselect;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.objects.Player;

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

    private static final String TAG = PlayerSelectAdapter.class.getSimpleName();

    private PlayerSelectContract.Presenter mPresenter;
    private ArrayList<Player> mPlayerArrayList;
    private int mType;


    public PlayerSelectAdapter(PlayerSelectContract.Presenter presenter, int type) {
        mPresenter = presenter;
        mPlayerArrayList = mPresenter.getPlayerOnCourt();
        mType = type;
    }

    public class DialogPlayerViewHolder extends RecyclerView.ViewHolder {

        private TextView mPlayerNumberTextView;
        private TextView mPlayerNameTextView;

        public DialogPlayerViewHolder(View itemView) {
            super(itemView);

            mPlayerNameTextView = itemView.findViewById(R.id.item_dialog_player_name_textview);
            mPlayerNumberTextView = itemView.findViewById(R.id.item_dialog_player_number_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.editDataInDB(mPlayerArrayList.get(getLayoutPosition()), mType);
                }
            });
        }

        private void bind(int position) {
            mPlayerNameTextView.setText(mPlayerArrayList.get(position).getName());
            mPlayerNumberTextView.setText(mPlayerArrayList.get(position).getNumber());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_player, parent, false);

        return new DialogPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DialogPlayerViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mPlayerArrayList.size();
    }

    /**
     * Deep clone ArrayList<Player>
     * 預留給未來不希望顯示名單保有 reference type 牽一髮動全身的特性再用
     *
     * @param originList input an ArrayList
     * @return clone ArrayList
     */
    private ArrayList<Player> deepClonePlayerList(ArrayList<Player> originList) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ArrayList<Player> returnPlayerList = originList;
        byte[] byteData = null;

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(originList);
            oos.flush();
            oos.close();
            bos.close();
            byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            returnPlayerList = (ArrayList<Player>) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException mE) {
            mE.printStackTrace();
        }
        return returnPlayerList;
    }
}
