package com.example.wade8.boxscore.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter{

    private final static int TITLE_VIEW = 0;
    private final static int PLAYER_VIEW = 1;

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;
    private ArrayList<Player> mUnregisteredPlayerList;
    private final int[] mTitle = {R.string.startingPlayers,R.string.substitutePlayers,R.string.unregisteredPlayers};


    public class TitleViewHolder extends RecyclerView.ViewHolder{

        public TitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class PlayersViewHolder extends  RecyclerView.ViewHolder{

        public PlayersViewHolder(View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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
