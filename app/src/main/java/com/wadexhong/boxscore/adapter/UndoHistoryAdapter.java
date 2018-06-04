package com.wadexhong.boxscore.adapter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.Undo;
import com.wadexhong.boxscore.undohistory.UndoHistoryContract;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryAdapter extends RecyclerView.Adapter{

    private static final String TAG = UndoHistoryAdapter.class.getSimpleName();

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

                    new AlertDialog.Builder(itemView.getContext(), R.style.OrangeDialog)
                              .setTitle("復原確認")
                              .setMessage("\n是否要對\n\n「 "+ mName.getText().toString()+" "+mType.getText().toString()+" 」\n\n進行復原 ?\n\n警告:\n　　 復原後將清除該項紀錄且不會另行顯示更動內容 !\n")
                              .setPositiveButton(R.string.yes, dialogClickListener)
                              .setNegativeButton(R.string.no, dialogClickListener).show();
                }
            });

            mConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    new AlertDialog.Builder(itemView.getContext(), R.style.OrangeDialog)
                              .setTitle("操作選單")
                              .setItems(new String[]{"標記","編輯","刪除"}, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {

                                      switch (which) {

                                          case 0:

                                              break;

                                          case 1:
                                              new AlertDialog.Builder(itemView.getContext(), R.style.OrangeDialog).setItems(Constants.TYPE_CHOICE_STRING, new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      mUndoHistoryPresenter.editAtPosition(getLayoutPosition());
                                                  }
                                              });
                                              break;

                                          case 2:
                                              break;
                                      }
                                  }
                              }).show();


                    return true;
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
