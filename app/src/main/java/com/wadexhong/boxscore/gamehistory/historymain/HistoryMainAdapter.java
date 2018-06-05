package com.wadexhong.boxscore.gamehistory.historymain;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/23.
 */

public class HistoryMainAdapter extends RecyclerView.Adapter {

    private static final String TAG = HistoryMainAdapter.class.getSimpleName();

    private HistoryMainContract.Presenter mHistoryMainPresenter;
    private Cursor mCursor;



    public HistoryMainAdapter(HistoryMainContract.Presenter historyMainPresenter){
        mHistoryMainPresenter = historyMainPresenter;

        refreshCursor();
    }

    public void refreshCursor(){
        mCursor = mHistoryMainPresenter.getGameHistory();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_history, parent, false);

        return new GameHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GameHistoryViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }



    public class GameHistoryViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private TextView mDate;
        private TextView mGameTitle;
        private TextView mYourTeam;
        private TextView mOpponentTeam;
        private TextView mFinalScore;
        private ImageView mShare;

        public GameHistoryViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.item_game_history_layout);
            mDate = itemView.findViewById(R.id.item_game_history_date);
            mGameTitle = itemView.findViewById(R.id.item_game_history_gametitle);
            mYourTeam = itemView.findViewById(R.id.item_game_history_yourteam);
            mOpponentTeam = itemView.findViewById(R.id.item_game_history_opponentteam);
            mFinalScore = itemView.findViewById(R.id.item_game_history_finalscore);
            mShare = itemView.findViewById(R.id.item_game_history_share);

        }

        private void bind (int position){

            mCursor.moveToPosition(position);

            final String gameId = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID));
            String date = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE));
            String gameTitle = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME));
            String yourTeam = itemView.getResources().getString(R.string.yourTeamName, mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM)));
            String opponentTeam = itemView.getResources().getString(R.string.opponentTeamName, mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)));
            String finalScore = itemView.getResources().getString(R.string.finalScore,
                      mCursor.getInt(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE)),
                      mCursor.getInt(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE)));

            if (gameTitle.equals("")){
                mGameTitle.setText("友誼賽");
            }else {
                mGameTitle.setText(gameTitle);
            }
            mDate.setText(date);
            mYourTeam.setText(yourTeam);
            mOpponentTeam.setText(opponentTeam);
            mFinalScore.setText(finalScore);
            mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"mShare onClick, gameId = "+ gameId);
//                    mHistoryMainPresenter.share(gameId);
                }
            });
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"mLayout onClick, gameId = "+ gameId);
                    mHistoryMainPresenter.transToDetail(gameId);
                }
            });

        }
    }
}
