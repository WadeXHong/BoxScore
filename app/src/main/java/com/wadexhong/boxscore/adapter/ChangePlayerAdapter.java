package com.wadexhong.boxscore.adapter;


import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.playeroncourt.ChangePlayerContract;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class ChangePlayerAdapter extends RecyclerView.Adapter{

    private final static String TAG = ChangePlayerAdapter.class.getSimpleName();
    private final ChangePlayerContract.Presenter mPresenter;

    private final static int TITLE_VIEW = 0;
    private final static int PLAYER_VIEW = 1;

    private ArrayList<Player> mPlayerOnCourtList;
    private ArrayList<Player> mBenchPlayerList;
    private final int[] mTitle = {R.string.playerOnCourt,R.string.benchPlayer};


    public ChangePlayerAdapter(ChangePlayerContract.Presenter mPresenter, ArrayList<Player> mPlayerOnCourtList, ArrayList<Player> mBenchPlayerList) {
        this.mPresenter = mPresenter;
        this.mPlayerOnCourtList = mPlayerOnCourtList;
        this.mBenchPlayerList = mBenchPlayerList;
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.item_playeroncourt_title_textview);
        }

        private void bindTitle(int position){
            if (position == 0){
                mTitleTextView.setText(mTitle[0]);
            } else {
                mTitleTextView.setText(mTitle[1]);
            }
        }
    }

    public class PlayersViewHolder extends  RecyclerView.ViewHolder{

        private ConstraintLayout mConstraintLayout;
        private TextView mPlayerNumber;
        private TextView mPlayerName;

        public PlayersViewHolder(View itemView) {
            super(itemView);

            mConstraintLayout = itemView.findViewById(R.id.item_playeroncourt_layout);
            mPlayerName = itemView.findViewById(R.id.item_playeroncourt_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playeroncourt_playernumber);


        }

        private void bindPlayers(int position){
            final int positionInOnCourtArray = position - 1;
            final int positionInBenchArray = position - mPlayerOnCourtList.size() -2;

            // 場下球員
            if (position > getArrayListSize(mPlayerOnCourtList)+1){
                if (getArrayListSize(mBenchPlayerList) != 0) {
                    mPlayerNumber.setText(mBenchPlayerList.get(positionInBenchArray).getNumber());
                    mPlayerName.setText(mBenchPlayerList.get(positionInBenchArray).getName());
                    mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //從場下球員選擇要上場的球員
                            mPresenter.offGamePlayerSelected(positionInBenchArray);
                        }
                    });
                }else {
                    Log.w(TAG,"mBenchPlayerList is null or empty !");
                }
            }
             //場上球員
              else {
                if (getArrayListSize(mPlayerOnCourtList) != 0) {
                    mPlayerName.setText(mPlayerOnCourtList.get(positionInOnCourtArray).getName());
                    mPlayerNumber.setText(mPlayerOnCourtList.get(positionInOnCourtArray).getNumber());
                    mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //從場上球員選擇要下場的球員
                            mPresenter.inGamePlayerSelected(positionInOnCourtArray);
                        }
                    });
                }else {
                    Log.w(TAG,"mPlayerOnCourtList is null or empty !");
                }
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TITLE_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playeroncourt_title, parent, false);
            return new TitleViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playeroncourt_players,parent,false);
            return new PlayersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).bindTitle(position);
        }else if (holder instanceof PlayersViewHolder){
            ((PlayersViewHolder) holder).bindPlayers(position);
        }else {
            Log.w(TAG,"onBindViewHolder position error !");
        }

    }

    @Override
    public int getItemCount() {
        return 2+getArrayListSize(mPlayerOnCourtList)+getArrayListSize(mBenchPlayerList);
    }


    @Override
    public int getItemViewType(int position) {
        int sizeStartingPlayers = getArrayListSize(mPlayerOnCourtList);
        int sizeSubstitutePlayers = getArrayListSize(mBenchPlayerList);
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

    public ArrayList<Player> getPlayerOnCourtList() {
        return mPlayerOnCourtList;
    }

    public ArrayList<Player> getBenchPlayerList() {
        return mBenchPlayerList;
    }


}
