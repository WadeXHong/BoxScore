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

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/23.
 */

public class HistoryMainAdapter extends RecyclerView.Adapter {

    private static final String TAG = HistoryMainAdapter.class.getSimpleName();

    private HistoryMainContract.Presenter mHistoryMainPresenter;
    private Cursor mCursor;


    public HistoryMainAdapter(HistoryMainContract.Presenter historyMainPresenter) {
        mHistoryMainPresenter = historyMainPresenter;

        refreshCursor();
    }

    public void refreshCursor() {
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
        ((GameHistoryViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public class GameHistoryViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private TextView mDateTextView;
        private TextView mGameTitleTextView;
        private TextView mYourTeamTextView;
        private TextView mOpponentTeamTextView;
        private TextView mFinalScoreTextView;
        private ImageView mShareImageView;

        public GameHistoryViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.item_game_history_layout);
            mDateTextView = itemView.findViewById(R.id.item_game_history_date);
            mGameTitleTextView = itemView.findViewById(R.id.item_game_history_gametitle);
            mYourTeamTextView = itemView.findViewById(R.id.item_game_history_yourteam);
            mOpponentTeamTextView = itemView.findViewById(R.id.item_game_history_opponentteam);
            mFinalScoreTextView = itemView.findViewById(R.id.item_game_history_finalscore);
            mShareImageView = itemView.findViewById(R.id.item_game_history_share);

        }

        private void bind(int position) {

            mCursor.moveToPosition(position);

            final String gameId = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID));
            String date = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE));
            String gameTitle = mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME));
            String yourTeam = BoxScore.getStringEasy(R.string.yourTeamName, mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM)));
            String opponentTeam = BoxScore.getStringEasy(R.string.opponentTeamName, mCursor.getString(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)));
            String finalScore = BoxScore.getStringEasy(R.string.finalScore,
                      mCursor.getInt(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE)),
                      mCursor.getInt(mCursor.getColumnIndex(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE)));

            if (gameTitle.equals("")) {
                mGameTitleTextView.setText("友誼賽");
            } else {
                mGameTitleTextView.setText(gameTitle);
            }

            mDateTextView.setText(date);
            mYourTeamTextView.setText(yourTeam);
            mOpponentTeamTextView.setText(opponentTeam);
            mFinalScoreTextView.setText(finalScore);
            mShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "mShareImageView onClick, gameId = " + gameId);
//                    mHistoryMainPresenter.share(gameId);
                }
            });
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "mLayout onClick, gameId = " + gameId);
                    mHistoryMainPresenter.transToDetail(gameId);
                }
            });
        }
    }
}
