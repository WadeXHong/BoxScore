package com.example.wade8.boxscore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.objects.Undo;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class HistoryAdapter extends RecyclerView.Adapter{

    private LinkedList<Undo> mUndoList;

    public HistoryAdapter(LinkedList<Undo> mUndoList) {
        this.mUndoList = mUndoList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
