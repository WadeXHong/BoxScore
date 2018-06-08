package com.wadexhong.boxscore.gameboxscore.datastatistic;

import android.database.Cursor;
import android.support.annotation.NonNull;
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

public class DataStatisticAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {

    private final String TAG = DataStatisticAdapter.class.getSimpleName();

    private final String GAME_ID;
    private Cursor mCursor;
    private int mGameDataStartColumnPosition;

    public DataStatisticAdapter(String gameId) {
        GAME_ID = gameId;
        freshCursor();
        mGameDataStartColumnPosition = mCursor.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")");
        Log.e(TAG, mGameDataStartColumnPosition + "");
    }

    private void freshCursor() {
        mCursor = BoxScore.getGameDataDbHelper()
                  .getHistoryStatistic(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID + " = ?",
                            new String[]{GAME_ID},
                            Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER,
                            Constants.GameDataDBContract.COLUMN_NAME_POINTS);
        mCursor.moveToFirst();
    }

    @Override
    public int getRowCount() {
        return mCursor.getCount() + 1;
    }

    @Override
    public int getColumnCount() {
        return mCursor.getColumnCount() - mGameDataStartColumnPosition + 1;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ColumnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        ((ViewHolder) viewHolder).bind(row - 1, column - 1);
    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        ((ColumnViewHolder) viewHolder).bind(column - 1);
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        ((RowViewHolder) viewHolder).bind(row - 1);
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        ((ViewHolder) viewHolder).mTextView.setText(R.string.name);
    }

    @Override
    public int getColumnWidth(int column) {
        return (int) BoxScore.getDimensionEasy(R.dimen.statistic_adapter_column_width);
    }

    @Override
    public int getHeaderColumnHeight() {
        return (int) BoxScore.getDimensionEasy(R.dimen.statistic_adapter_header_column_height);
    }

    @Override
    public int getRowHeight(int row) {
        return (int) BoxScore.getDimensionEasy(R.dimen.statistic_adapter_row_height);
    }

    @Override
    public int getHeaderRowWidth() {
        return (int) BoxScore.getDimensionEasy(R.dimen.statistic_adapter_header_row_width);
    }


    private class ViewHolder extends ViewHolderImpl {

        private TextView mTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind(int row, int column) {
            //mCursor 包含 gameID , quarter等不需顯示的資料, 因此第一欄改為由"PTS"顯示
            int columnGameDataOnly = column + mGameDataStartColumnPosition;
            mCursor.moveToPosition(row);
            mTextView.setText(String.valueOf(mCursor.getInt(columnGameDataOnly)));


        }
    }

    private class ColumnViewHolder extends ViewHolderImpl {

        private final String TAG = ColumnViewHolder.class.getSimpleName();
        private TextView mTextView;

        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind(int column) {

            mTextView.setText(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.COLUMN_NAME_SPARSE_ARRAY.keyAt(column)));
            Log.d(TAG, "bind COLUMN_NAME_SPARSE_ARRAY at column = " + (column));
        }
    }


    private class RowViewHolder extends ViewHolderImpl {

        private final String TAG = RowViewHolder.class.getSimpleName();
        private TextView mTextView;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_datastatistic_textview);
        }

        private void bind(int row) {

            mCursor.moveToPosition(row);
            String name = mCursor.getString(mCursor.getColumnIndex(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME));
            mTextView.setText(name);
            Log.d(TAG, "bind name at row = " + (row));
        }
    }
}
