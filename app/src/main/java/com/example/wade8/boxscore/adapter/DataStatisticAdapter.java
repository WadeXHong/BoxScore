package com.example.wade8.boxscore.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;

/**
 * Created by wade8 on 2018/5/8.
 */

public class DataStatisticAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl>{
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return null;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return null;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return null;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {

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
        return 0;
    }

    @Override
    public int getHeaderColumnHeight() {
        return 0;
    }

    @Override
    public int getRowHeight(int row) {
        return 0;
    }

    @Override
    public int getHeaderRowWidth() {
        return 0;
    }
}
