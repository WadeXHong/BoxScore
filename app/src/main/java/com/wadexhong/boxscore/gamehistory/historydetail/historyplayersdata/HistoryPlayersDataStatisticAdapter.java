package com.wadexhong.boxscore.gamehistory.historydetail.historyplayersdata;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/8.
 */

public class HistoryPlayersDataStatisticAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl>{

    private static final String TAG = HistoryPlayersDataStatisticAdapter.class.getSimpleName();

    private HistoryPlayersDataContract.Presenter mHistoryPlayersDataPresenter;

    private int mQuarterFilter;
    private String mGameId;
    private Cursor mCursor;
    private int mGameDataStartColumnPosition;


    public HistoryPlayersDataStatisticAdapter(HistoryPlayersDataContract.Presenter presenter) {
        mHistoryPlayersDataPresenter = presenter;
        refreshCursor(mGameId);
    }

    public void refreshCursor(String gameId) {
        if (gameId != null) {
            if (mGameId != null && !mGameId.equals(gameId)) {
                mQuarterFilter = 0;
                notifyItemChanged(0,0);
            }

            mGameId = gameId;

            if (mQuarterFilter != 0) {
                mCursor = BoxScore.getGameDataDbHelper()
                          .getHistoryStatistic(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =? AND " + Constants.GameDataDBContract.COLUMN_NAME_QUARTER + " =?",
                                    new String[]{mGameId, String.valueOf(mQuarterFilter)}, //要playerName, 因為預設隊伍的player沒有id
                                    Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER);
            } else {
                mCursor = BoxScore.getGameDataDbHelper()
                          .getHistoryStatistic(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " =?",
                                    new String[]{mGameId},
                                    Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER);
            }

            notifyDataSetChanged();
            mGameDataStartColumnPosition = mCursor.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")");
            Log.e(TAG,mGameDataStartColumnPosition+"");
        }
    }

    public void chooseFilter(int quarterFilter){
        mQuarterFilter = quarterFilter;
        refreshCursor(mGameId);
        notifyItemChanged(0,0);
    }

    @Override
    public int getRowCount() {
        if (mGameId == null){
            return 0;
        }else {
            return mCursor.getCount() + 1;
        }
    }

    @Override
    public int getColumnCount() {
        if (mGameId == null){
            return 0;
        }else {
            return mCursor.getColumnCount() - mGameDataStartColumnPosition + 1;
        }
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic_lighttext,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ColumnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic_lighttext,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic_lighttext,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new LeftTopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic_filter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        if (mGameId != null) ((ViewHolder)viewHolder).bind(row-1,column-1);
    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        if (mGameId != null) ((ColumnViewHolder)viewHolder).bind(column-1);
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        if (mGameId != null) ((RowViewHolder)viewHolder).bind(row-1);
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        if (mGameId != null) ((LeftTopViewHolder)viewHolder).bind();
    }

    @Override
    public int getColumnWidth(int column) {
        return 125;
    }

    @Override
    public int getHeaderColumnHeight() {
        return 75;
    }

    @Override
    public int getRowHeight(int row) {
        return 100;
    }

    @Override
    public int getHeaderRowWidth() {
        return 200;
    }


    private class ViewHolder extends ViewHolderImpl {

        private TextView mTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind (int row, int column){
            //mCursor 包含 gameID , quarter等不需顯示的資料, 因此第一欄改為由"PTS"顯示
            int columnGameDataOnly = column + mGameDataStartColumnPosition;
            mCursor.moveToPosition(row);
            mTextView.setText(String.valueOf(mCursor.getInt(columnGameDataOnly)));


        }
    }

    private class ColumnViewHolder extends ViewHolderImpl{

        private final String TAG = ColumnViewHolder.class.getSimpleName();
        private TextView mTextView;

        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind (int column){

            mTextView.setText(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.COLUMN_NAME_SPARSE_ARRAY.keyAt(column)));
            Log.d(TAG,"bind COLUMN_NAME_SPARSE_ARRAY at column = "+(column));
        }
    }


    private class RowViewHolder extends ViewHolderImpl{

        private final String TAG = RowViewHolder.class.getSimpleName();
        private TextView mTextView;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind (int row){

            mCursor.moveToPosition(row);
            String name = mCursor.getString(mCursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME));
            mTextView.setText(name);
            Log.d(TAG,"bind name at row = "+(row));
        }
    }

    private class LeftTopViewHolder extends ViewHolderImpl{

        private final int[] charSequence;
        private ConstraintLayout mLayout;
        private TextView mTextView;

        public LeftTopViewHolder(@NonNull View itemView) {
            super(itemView);
            charSequence = new int[]{R.string.total, R.string.firstQuarter, R.string.secondQuarter, R.string.thirdQuarter, R.string.forthQuarter};
            mTextView = itemView.findViewById(R.id.item_datastatistic_filter_text);
            mLayout = itemView.findViewById(R.id.item_datastatistic_filter_layout);

        }

        private void bind() {
            mTextView.setText(charSequence[mQuarterFilter]);
        }
    }
}
