package com.example.wade8.boxscore.adapter;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.TeamDetail;
import com.example.wade8.boxscore.objects.TeamInfo;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by wade8 on 2018/5/20.
 */

public class TeamAdapter extends ExpandableRecyclerViewAdapter<TeamAdapter.TeamViewHolder, TeamAdapter.DetailViewHolder> {

    public TeamAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public TeamViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_teamname, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public DetailViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_teamdetail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(DetailViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final TeamDetail detail = ((TeamInfo)group).getItems().get(childIndex);
        holder.setView(detail);
    }

    @Override
    public void onBindGroupViewHolder(TeamViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group);
    }

    public class TeamViewHolder extends GroupViewHolder{

        private ImageView mDelete;
        private ImageView mArrow;
        private TextView mTeamName;

        public TeamViewHolder(View itemView) {
            super(itemView);
            mDelete = itemView.findViewById(R.id.item_team_teamname_delete);
            mArrow = itemView.findViewById(R.id.item_team_teamname_icon);
            mTeamName = itemView.findViewById(R.id.item_team_teamname_teamname);
        }

        public void setTitle(ExpandableGroup group){
            mTeamName.setText(group.getTitle());
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                      new RotateAnimation(0, 90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(150);
            rotate.setFillAfter(true);
            mArrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                      new RotateAnimation(90, 0, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(150);
            rotate.setFillAfter(true);
            mArrow.setAnimation(rotate);
        }
    }

    public class DetailViewHolder extends ChildViewHolder{
        private final String TAG = DetailViewHolder.class.getSimpleName();

        private ConstraintLayout mPlayersLayout;
        private ConstraintLayout mHistoryLayout;
        private TextView mPlayersAmount;
        private TextView mHistoryAmount;

        public DetailViewHolder(View itemView) {
            super(itemView);
            mPlayersLayout = itemView.findViewById(R.id.item_team_teamdetail_players_layout);
            mHistoryLayout = itemView.findViewById(R.id.item_team_teamdetail_history_layout);
            mPlayersAmount = itemView.findViewById(R.id.item_team_teamdetail_players_textview);
            mHistoryAmount = itemView.findViewById(R.id.item_team_teamdetail_history_textview);
        }
        public void setView(TeamDetail detail){
            mHistoryAmount.setText(String.format(itemView.getContext().getResources().getString(R.string.historyAmount),detail.getTeamHistoryAmount()));
            mPlayersAmount.setText(String.format(itemView.getContext().getResources().getString(R.string.playersAmount),detail.getTeamPlayersAmount()));
            mHistoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"history layout pressed");
                }
            });
            mPlayersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"players layout pressed");
                }
            });

        }
    }
}
