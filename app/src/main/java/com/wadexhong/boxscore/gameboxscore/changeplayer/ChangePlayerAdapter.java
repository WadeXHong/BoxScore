package com.wadexhong.boxscore.gameboxscore.changeplayer;


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

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class ChangePlayerAdapter extends RecyclerView.Adapter {

    private final static String TAG = ChangePlayerAdapter.class.getSimpleName();
    private final ChangePlayerContract.Presenter mPresenter;

    private final static int VIEW_TYPE_TITLE = 0;
    private final static int VIEW_TYPE_PLAYER = 1;

    private ArrayList<Player> mPlayerOnCourtList;
    private ArrayList<Player> mBenchPlayerList;

    public ChangePlayerAdapter(ChangePlayerContract.Presenter presenter, ArrayList<Player> playerOnCourtList, ArrayList<Player> benchPlayerList) {
        mPresenter = presenter;
        mPlayerOnCourtList = playerOnCourtList;
        mBenchPlayerList = benchPlayerList;
    }

    private int getArrayListSize(ArrayList<Player> arrayList) {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_TITLE) {

            return new TitleViewHolder(LayoutInflater
                      .from(parent.getContext())
                      .inflate(R.layout.item_playeroncourt_title, parent, false));

        } else if (viewType == VIEW_TYPE_PLAYER) {

            return new PlayersViewHolder(LayoutInflater
                      .from(parent.getContext())
                      .inflate(R.layout.item_playeroncourt_players, parent, false));

        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {

            ((TitleViewHolder) holder).bindTitle(position);

        } else if (holder instanceof PlayersViewHolder) {

            ((PlayersViewHolder) holder).bindPlayers(position);

        } else {
            Log.w(TAG, "onBindViewHolder position error !");
        }

    }

    @Override
    public int getItemCount() {
        // 先發替補球員數量總和兩欄標題
        return 2 + getArrayListSize(mPlayerOnCourtList) + getArrayListSize(mBenchPlayerList);
    }

    @Override
    public int getItemViewType(int position) {

        int sizeStartingPlayers = getArrayListSize(mPlayerOnCourtList);
        int sizeSubstitutePlayers = getArrayListSize(mBenchPlayerList);

        if (position == 0 | position == sizeStartingPlayers + 1 | position == sizeStartingPlayers + sizeSubstitutePlayers + 2) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_PLAYER;
        }
    }


    public class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.item_playeroncourt_title_textview);
        }
        private void bindTitle(int position) {
            if (position == 0) {
                mTitleTextView.setText(R.string.playerOnCourt);
            } else {
                mTitleTextView.setText(R.string.benchPlayer);
            }
        }

    }

    public class PlayersViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mConstraintLayout;
        private TextView mPlayerNumberTextView;
        private TextView mPlayerNameTextView;

        public PlayersViewHolder(View itemView) {
            super(itemView);

            mConstraintLayout = itemView.findViewById(R.id.item_playeroncourt_layout);
            mPlayerNameTextView = itemView.findViewById(R.id.item_playeroncourt_playername);
            mPlayerNumberTextView = itemView.findViewById(R.id.item_playeroncourt_playernumber);
        }

        private void bindPlayers(int position) {
            // 扣除標題
            final int positionInOnCourtArray = position - 1;
            final int positionInBenchArray = position - mPlayerOnCourtList.size() - 2;

            // 場下球員
            if (position > getArrayListSize(mPlayerOnCourtList) + 1) {

                if (getArrayListSize(mBenchPlayerList) != 0) {

                    mPlayerNumberTextView.setText(mBenchPlayerList.get(positionInBenchArray).getNumber());
                    mPlayerNameTextView.setText(mBenchPlayerList.get(positionInBenchArray).getName());
                    mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //從場下球員選擇要上場的球員
                            mPresenter.offGamePlayerSelected(positionInBenchArray);
                        }
                    });
                } else {
                    Log.w(TAG, "mBenchPlayerList is null or empty !");
                }
            }
            //場上球員
            else {

                if (getArrayListSize(mPlayerOnCourtList) != 0) {

                    mPlayerNameTextView.setText(mPlayerOnCourtList.get(positionInOnCourtArray).getName());
                    mPlayerNumberTextView.setText(mPlayerOnCourtList.get(positionInOnCourtArray).getNumber());
                    mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //從場上球員選擇要下場的球員
                            mPresenter.inGamePlayerSelected(positionInOnCourtArray);
                        }
                    });
                } else {
                    Log.w(TAG, "mPlayerOnCourtList is null or empty !");
                }
            }
        }
    }
}
