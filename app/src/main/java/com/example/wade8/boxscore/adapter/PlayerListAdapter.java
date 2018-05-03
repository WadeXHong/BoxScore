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
import android.widget.Toast;

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

    public PlayerListAdapter(){

        fakeDataInit();
    }

    private void fakeDataInit() {
        mStartingPlayerList = new ArrayList<>();
        mSubstitutePlayerList = new ArrayList<>();
        mUnregisteredPlayerList = new ArrayList<>();
        int x = 0;
        for (int i = 0; i<20;i++){
            mUnregisteredPlayerList.add(new Player(i+"",x+""));
            x++;
        }
    }


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
            mStarting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() > getArrayListSize(mStartingPlayerList)+1) {
                        if (getArrayListSize(mStartingPlayerList) < 5) {
                            int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;
                            mStartingPlayerList.add(mSubstitutePlayerList.get(position));
                            mSubstitutePlayerList.remove(position);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(v.getContext(),R.string.startingPlayerLimitToast,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        int position = getLayoutPosition() - 1;
                        mSubstitutePlayerList.add(0,mStartingPlayerList.get(position));
                        mStartingPlayerList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getLayoutPosition() > getArrayListSize(mStartingPlayerList)+1) {
                        int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;
                        mUnregisteredPlayerList.add(0,mSubstitutePlayerList.get(position));
                        mSubstitutePlayerList.remove(position);
                        notifyDataSetChanged();
                    }else{
                        int position = getLayoutPosition() - 1;
                            mUnregisteredPlayerList.add(0,mStartingPlayerList.get(position));
                            mStartingPlayerList.remove(position);
                            notifyDataSetChanged();
                    }
                }
            });

        }

        private void bindPlayers(int position){
            int positionInStartingArray = position - 1;
            int positionInSubstituteArray = position - getArrayListSize(mStartingPlayerList)-2;

            // 替補球員
            if (position > getArrayListSize(mStartingPlayerList)+1){
                if (getArrayListSize(mSubstitutePlayerList) != 0) {
                    mPlayerNumber.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getmNumber());
                    mPlayerName.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getmName());
                    mStarting.setImageResource(R.drawable.ic_star_gray_24dp);
                }else {
                    Log.w(TAG,"mSubstitutePlayerList is null or empty !");
                }
            }
             //先發球員
              else {
                if (getArrayListSize(mStartingPlayerList) != 0) {
                    mPlayerName.setText(mStartingPlayerList.get(positionInStartingArray).getmName());
                    mPlayerNumber.setText(mStartingPlayerList.get(positionInStartingArray).getmNumber());
                    mStarting.setImageResource(R.drawable.ic_star_yellow_24dp);
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
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mStartingPlayerList) < 15) {
                        int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - getArrayListSize(mSubstitutePlayerList) - 3;
                        mSubstitutePlayerList.add(mUnregisteredPlayerList.get(position));
                        mUnregisteredPlayerList.remove(position);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(v.getContext(),R.string.playerLimitToast,Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        private void bindUnregiseteredPlayers(int position){
            int positionInUnregiseteredArray = position -getArrayListSize(mStartingPlayerList)-getArrayListSize(mSubstitutePlayerList) - 3;

            if (getArrayListSize(mUnregisteredPlayerList) != 0) {
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
        return 3+getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList);
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

    public ArrayList<Player> getStartingPlayerList() {
        return mStartingPlayerList;
    }

    public ArrayList<Player> getSubstitutePlayerList() {
        return mSubstitutePlayerList;
    }

    public ArrayList<Player> getUnregisteredPlayerList() {
        return mUnregisteredPlayerList;
    }

}
