package com.example.wade8.boxscore.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataContract;
import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by wade8 on 2018/5/23.
 */

public class HistoryTeamDataAdapter extends RecyclerView.Adapter{

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_TABLE = 1;
    private static final int VIEWTYPE_GRAPH = 2;
    private static final int DATA_TYPE_COUNT = 14;
    private static final int COLUMN_POSITION_DISPLACEMENT = 5;

    private HistoryTeamDataContract.Presenter mHistoryTeamDataPresenter;

    private String mGameId;
    private Cursor mCursorData;
    private Cursor mCursorInfo;

    public HistoryTeamDataAdapter(HistoryTeamDataContract.Presenter presenter){
        mHistoryTeamDataPresenter = presenter;

        refreshCursor(mGameId);
    }



    public void refreshCursor(String gameId) {
        if (gameId != null) {
            mGameId = gameId;
            mCursorData = mHistoryTeamDataPresenter.getHistoryStatistic(mGameId);
            mCursorInfo = mHistoryTeamDataPresenter.getHistoryInfo(mGameId);
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_TABLE){
            return new TableViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_table, parent, false));
        }else if (viewType == VIEWTYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_header, parent, false));
        }else {
            return new GraphViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_chart, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mGameId != null) {
            if (position == 0) {
                ((HeaderViewHolder) holder).bind();
            }else if (position == DATA_TYPE_COUNT +1 ){
                //TODO
            }else {
                ((TableViewHolder) holder).bind(position - 1); //Todo position change
            }
        }

    }

    @Override
    public int getItemCount() {
        return DATA_TYPE_COUNT + 2; //data types showing + header and graph
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_HEADER;
        }else if (position == DATA_TYPE_COUNT+1) {
            return VIEWTYPE_GRAPH;
        }else {
            return VIEWTYPE_TABLE;
        }
    }



    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private TextView mYourTeamScore;
        private TextView mOpponentTeamScore;
        private TextView mStatus;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mYourTeamScore = itemView.findViewById(R.id.item_history_teamdata_yourteamscore);
            mOpponentTeamScore = itemView.findViewById(R.id.item_history_teamdata_opponentteamscore);
            mStatus = itemView.findViewById(R.id.item_history_teamdata_status);
        }

        private void bind(){

            mCursorInfo.moveToFirst();
            int yourTeamScore = mCursorInfo.getInt(mCursorInfo.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM_SCORE));
            int opponentTeamScore = mCursorInfo.getInt(mCursorInfo.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE));
            mYourTeamScore.setText(String.valueOf(yourTeamScore));
            mOpponentTeamScore.setText(String.valueOf(opponentTeamScore));
            if (yourTeamScore >= opponentTeamScore){
                mStatus.setText(R.string.win);
                mStatus.setTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorRed, null));
            }else {
                mStatus.setText(R.string.lose);
                mStatus.setTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorGreen, null));
            }
        }
    }



    public class TableViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewType;
        private TextView mTextView1;
        private TextView mTextView2;
        private TextView mTextView3;
        private TextView mTextView4;
        private TextView mTextViewTotal;





        public TableViewHolder(View itemView) {
            super(itemView);

            mTextViewType = itemView.findViewById(R.id.item_history_teamdata_table_type);
            mTextView1 = itemView.findViewById(R.id.item_history_teamdata_table_1st);
            mTextView2 = itemView.findViewById(R.id.item_history_teamdata_table_2nd);
            mTextView3 = itemView.findViewById(R.id.item_history_teamdata_table_3rd);
            mTextView4 = itemView.findViewById(R.id.item_history_teamdata_table_4th);
            mTextViewTotal = itemView.findViewById(R.id.item_history_teamdata_table_total);

        }

        private void bind(int position){

            mTextViewType.setText(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.COLUMN_NAME_SPARSE_ARRAY.keyAt(position)));


            int size = mCursorData.getCount();
            int first = 0, second = 0, third = 0, forth = 0, fifth = 0;

            for (int i = 0 ; i<size ; i++) {
                mCursorData.moveToPosition(i);
                switch (i) {
                    case 0:
                        first = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView1.setText(String.valueOf(first));
                    case 1:
                        second = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView2.setText(String.valueOf(second));
                    case 2:
                        third = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView3.setText(String.valueOf(third));
                    case 3:
                        forth = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView4.setText(String.valueOf(forth));
                    default:
                        fifth = first + second + third + forth;
                }
            }
            mTextViewTotal.setText(String.valueOf(fifth));

        }

    }


    public class GraphViewHolder extends RecyclerView.ViewHolder{

        private PieChart mPieChart;

        public GraphViewHolder(View itemView) {
            super(itemView);

            mPieChart = itemView.findViewById(R.id.chart);

        }
    }

}
