package com.wadexhong.boxscore.teammanage.teamplayers;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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
 * Created by wade8 on 2018/5/21.
 */

public class TeamPlayersAdapter extends RecyclerView.Adapter {

    private static final String TAG = TeamPlayersAdapter.class.getSimpleName();

    private TeamPlayersContract.Presenter mTeamPlayersPresenter;

    private String mTeamId;
    private Cursor mCursor;


    public TeamPlayersAdapter(String teamId, TeamPlayersContract.Presenter teamPlayersPresenter) {

        mTeamPlayersPresenter = teamPlayersPresenter;
        mTeamId = teamId;
        refreshCursor();

    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teamplayers_players, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setItem(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private TextView mPlayerName;
        private TextView mPlayerNumber;
        private ImageView mEdit;
        private ImageView mDelete;

        private ViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.item_teamplayers_layout);
            mPlayerName = itemView.findViewById(R.id.item_teamplayers_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_teamplayers_playernumber);
            mEdit = itemView.findViewById(R.id.item_teamplayers_edit);
            mDelete = itemView.findViewById(R.id.item_teamplayers_delete);

        }

        private void setItem(int position) {

            mCursor.moveToPosition(position);
            final String playerName = mCursor.getString(mCursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME));
            String playerNumber = mCursor.getString(mCursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER));

            mPlayerName.setText(playerName);
            mPlayerNumber.setText(playerNumber);

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "edit pressed");
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "delete pressed");

                    new AlertDialog.Builder(v.getContext(), R.style.OrangeDialog)
                              .setTitle(R.string.dialog_delete_player_title)
                              .setMessage(BoxScore.getStringEasy(R.string.dialog_delete_player_message, playerName))
                              .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      deletePlayer(getLayoutPosition());
                                  }
                              })
                              .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.cancel();
                                  }
                              }).show();
                }
            });

        }


        private void deletePlayer(int layoutPosition) {
            Log.d(TAG, "deletePlayer " + layoutPosition);
            mCursor.moveToPosition(layoutPosition);

            String playerId = mCursor.getString(mCursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID));
            String teamId = mCursor.getString(mCursor.getColumnIndex(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID));

            mTeamPlayersPresenter.deletePlayer(teamId, playerId);
            refreshCursor();
        }
    }

    public void refreshCursor() {
        Log.d(TAG, "refreshCursor, teamId = " + mTeamId);
        mCursor = mTeamPlayersPresenter.getPlayers(mTeamId);
        notifyDataSetChanged();
    }

}
