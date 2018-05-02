package com.example.wade8.boxscore.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.gamenamesetting.GameNameSettingContract;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter{

    private final static String TAG = PlayerListAdapter.class.getSimpleName();

    private final static int TITLE_VIEW = 0;
    private final static int PLAYER_VIEW = 1;
    private final static int UNREGISTERED_PLAYER_VIEW = 2;

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;
    private ArrayList<Player> mUnregisteredPlayerList;
    private final int[] mTitle = {R.string.startingPlayers,R.string.substitutePlayers,R.string.unregisteredPlayers};


    public class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.item_playerlist_title_textview);
        }

        private void bindTitle(int position){
            if (position == 0){
                mTitleTextView.setText(mTitle[0]);
            }else if (position>0 && position<getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+2){
                mTitleTextView.setText(mTitle[1]);
            }else {
                mTitleTextView.setText(mTitle[2]);
            }
        }
    }

    public class PlayersViewHolder extends  RecyclerView.ViewHolder{

        private TextView mPlayerNumber;
        private TextView mPlayerName;
        private ImageView mRemove;
        private ImageView mStarting;

        public PlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playerlist_playernumber);
            mRemove = itemView.findViewById(R.id.item_playerlist_remove);
            mStarting = itemView.findViewById(R.id.item_playerlist_star);

        }

        private void bindPlayers(int position){
            int positionInStartingArray = position - 1;
            int positionInSubstituteArray = position - getArrayListSize(mStartingPlayerList)-2;

            // 替補球員
            if (position > getArrayListSize(mStartingPlayerList)+1){
                if (getArrayListSize(mSubstitutePlayerList) != 0) {
                    mPlayerNumber.setText(mSubstitutePlayerList.get(positionInStartingArray).getmNumber());
                    mPlayerName.setText(mSubstitutePlayerList.get(positionInStartingArray).getmName());
                }else {
                    Log.w(TAG,"mSubstitutePlayerList is null or empty !");
                }
            }
             //先發球員
              else {
                if (getArrayListSize(mStartingPlayerList) != 0) {
                    mPlayerName.setText(mStartingPlayerList.get(positionInSubstituteArray).getmName());
                    mPlayerNumber.setText(mStartingPlayerList.get(positionInSubstituteArray).getmNumber());
                }else {
                    Log.w(TAG,"mStartingPlayerList is null or empty !");
                }
            }
        }
    }

    public class UnregisteredPlayersViewHolder extends RecyclerView.ViewHolder{

        private TextView mPlayerNumber;
        private TextView mPlayerName;
        private ImageView mAdd;

        public UnregisteredPlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playerlist_playernumber);
            mAdd = itemView.findViewById(R.id.item_playerlist_add);
        }

        private void bindUnregiseteredPlayers(int position){
            int positionInUnregiseteredArray = position -getArrayListSize(mStartingPlayerList)-getArrayListSize(mSubstitutePlayerList) - 3;

            if (getArrayListSize(mSubstitutePlayerList) != 0) {
                mPlayerName.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getmName());
                mPlayerNumber.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getmNumber());
            }else {
                Log.w(TAG,"mSubstitutePlayerList is null or empty !");
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TITLE_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_title, parent, false);
            return new TitleViewHolder(view);
        }else if (viewType == UNREGISTERED_PLAYER_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_unregisteredplayers, parent, false);
            return new UnregisteredPlayersViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_players,parent,false);
            return new PlayersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).bindTitle(position);
        }else if (holder instanceof PlayersViewHolder){
            ((PlayersViewHolder) holder).bindPlayers(position);
        }else if (holder instanceof UnregisteredPlayersViewHolder){
            ((UnregisteredPlayersViewHolder) holder).bindUnregiseteredPlayers(position);
        }else {
            Log.w(TAG,"onBindViewHolder position error !");
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        int sizeStartingPlayers = getArrayListSize(mStartingPlayerList);
        int sizeSubstitutePlayers = getArrayListSize(mSubstitutePlayerList);
        int returnValue = 1;
        if (position == 0 | position == sizeStartingPlayers+1 | position == sizeStartingPlayers+sizeSubstitutePlayers+2){
            returnValue = 0;
        }else if (position >sizeStartingPlayers+sizeSubstitutePlayers+2){
            returnValue = 2;
        }

        return returnValue;
    }


    private int getArrayListSize(ArrayList<Player> arrayList){
        int returnValue = 0;
        if (arrayList != null){
            returnValue = arrayList.size();
        }
        return returnValue;
    }

}
