package com.example.wade8.boxscore.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataContract;

/**
 * Created by wade8 on 2018/5/23.
 */

public class HistoryTeamDataAdapter extends RecyclerView.Adapter{

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_TABLE = 1;
    private static final int COLUMN_POSITION_DISPLACEMENT = 5;

    private HistoryTeamDataContract.Presenter mHistoryTeamDataPresenter;

    private String mGameId;
    private Cursor mCursor;

    public HistoryTeamDataAdapter(HistoryTeamDataContract.Presenter presenter, String gameId){
        mGameId = gameId;
        mHistoryTeamDataPresenter = presenter;

        refreshCursor(mGameId);
    }

    public void refreshCursor(String gameId) {
        mGameId = gameId;
        mCursor = mHistoryTeamDataPresenter.getHistoryStatistic(mGameId);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_header, parent, false));
        }else {
            return new TableViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_table, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            ((HeaderViewHolder)holder).bind();
        }else {
            ((TableViewHolder)holder).bind(position-1); //Todo position change
        }

    }

    @Override
    public int getItemCount() {
        return 15; //TODO 圖表
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_HEADER;
        }else {
            return VIEWTYPE_TABLE;
        }
    }



    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        private void bind(){

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


            int size = mCursor.getCount();
            int first = 0, second = 0, third = 0, forth = 0, fifth = 0;

            for (int i = 0 ; i<size ; i++) {
                mCursor.moveToPosition(i);
                switch (i) {
                    case 0:
                        first = mCursor.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView1.setText(String.valueOf(first));
                    case 1:
                        second = mCursor.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView2.setText(String.valueOf(second));
                    case 2:
                        third = mCursor.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView3.setText(String.valueOf(third));
                    case 3:
                        forth = mCursor.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView4.setText(String.valueOf(forth));
                    default:
                        fifth = first + second + third + forth;
                }
            }
            mTextViewTotal.setText(String.valueOf(fifth));

        }
    }

}
