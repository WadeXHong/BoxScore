package com.example.wade8.boxscore.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;

/**
 * Created by wade8 on 2018/5/8.
 */

public class DataStatisticAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl>{


    private Cursor mCursor;

    public DataStatisticAdapter() {
        freshCursor();
    }

    private void freshCursor() {
        mCursor = BoxScore.getGameDataDbHelper().getGameStatisic();
    }

    @Override
    public int getRowCount() {
        return mCursor.getCount()+1;
    }

    @Override
    public int getColumnCount() {
        return mCursor.getColumnCount()+1;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datastatistic,parent,false));
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

    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {

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
            mCursor.moveToPosition(row);
            switch (column){
                case 1:
                    break;
                case 0:
                case 3:
                    mTextView.setText(mCursor.getString(column));
                    break;
                default:
                    mTextView.setText(String.valueOf(mCursor.getInt(column)));

            }
        }
    }

}
