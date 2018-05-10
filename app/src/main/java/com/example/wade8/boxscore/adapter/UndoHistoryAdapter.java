package com.example.wade8.boxscore.adapter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.Undo;
import com.example.wade8.boxscore.undohistory.UndoHistoryContract;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryAdapter extends RecyclerView.Adapter{

    private UndoHistoryContract.Presenter mUndoHistoryPresenter;

    private LinkedList<Undo> mUndoList;

    public UndoHistoryAdapter(UndoHistoryContract.Presenter mPresenter, LinkedList<Undo> mUndoList) {
        mUndoHistoryPresenter = mPresenter;
        this.mUndoList = mUndoList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout mConstraintLayout;
        private TextView mQuarter;
        private TextView mNumber;
        private TextView mName;
        private TextView mType;

        public ViewHolder(View itemView) {
            super(itemView);
            mConstraintLayout = itemView.findViewById(R.id.item_undohistory_layout);
            mQuarter = itemView.findViewById(R.id.item_undohistory_quarter);
            mNumber = itemView.findViewById(R.id.item_undohistory_number);
            mName = itemView.findViewById(R.id.item_undohistory_name);
            mType = itemView.findViewById(R.id.item_undohistory_type);
        }

        private void bind (int position){
            Undo undo = mUndoList.get(position);
            mQuarter.setText(String.valueOf(undo.getQuarter()));
            mNumber.setText(undo.getPlayer().getNumber());
            mName.setText(undo.getPlayer().getName());
            mType.setText(Constants.TITLE_SPARSE_ARRAY.get(undo.getType()));

            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            mUndoHistoryPresenter.undoAtPosition(getLayoutPosition());
                            break;
                    }
                }
            };

            mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(itemView.getContext(),R.style.Theme_AppCompat_Light_Dialog)
                              .setTitle("復原確認")
                              .setMessage("是否要對\n"+ mName.getText().toString()+" "+mType.getText().toString()+"\n進行復原?\n警告:復原後將清除該項紀錄且不會另行顯示更動內容")
                              .setPositiveButton(R.string.yes, dialogClickListener)
                              .setNegativeButton(R.string.no, dialogClickListener).show();
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_undohistory,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mUndoList.size();
    }
}
