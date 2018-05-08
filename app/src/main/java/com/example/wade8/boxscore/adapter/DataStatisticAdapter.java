package com.example.wade8.boxscore.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;

import org.w3c.dom.Text;

/**
 * Created by wade8 on 2018/5/8.
 */

public class DataStatisticAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl>{


    private Cursor mCursor;
    private int mGameDataStartColumnPosition;

    public DataStatisticAdapter() {
        freshCursor();
        mGameDataStartColumnPosition = mCursor.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_POINTS + ")");
        Log.e("幹",mGameDataStartColumnPosition+"");
    }

    private void freshCursor() {
        mCursor = BoxScore.getGameDataDbHelper().getGameStatisic();
        mCursor.moveToFirst();
    }

    @Override
    public int getRowCount() {
        return mCursor.getCount()+1;
    }

    @Override
    public int getColumnCount() {
        return mCursor.getColumnCount() - mGameDataStartColumnPosition+1;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ColumnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        ((ViewHolder)viewHolder).bind(row-1,column-1);
    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        ((ColumnViewHolder)viewHolder).bind(column-1);
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        ((RowViewHolder)viewHolder).bind(row-1);
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {

    }

    @Override
    public int getColumnWidth(int column) {
        return 300;
    }

    @Override
    public int getHeaderColumnHeight() {
        return 300;
    }

    @Override
    public int getRowHeight(int row) {
        return 300;
    }

    @Override
    public int getHeaderRowWidth() {
        return 300;
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
}
