package com.wadexhong.boxscore.gameboxscore.undohistory;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.spinner.SelectPlayerAdapter;
import com.wadexhong.boxscore.adapter.spinner.SelectTypeAdapter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.Undo;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryAdapter extends RecyclerView.Adapter{

    private static final String TAG = UndoHistoryAdapter.class.getSimpleName();

    private UndoHistoryContract.Presenter mUndoHistoryPresenter;

    private LinkedList<Undo> mUndoList;
    private GameInfo mGameInfo;
    private final String[] TYPE_CHOICE_STRING;

    public UndoHistoryAdapter(UndoHistoryContract.Presenter mPresenter, LinkedList<Undo> mUndoList, GameInfo gameInfo) {
        mUndoHistoryPresenter = mPresenter;
        this.mUndoList = mUndoList;
        mGameInfo = gameInfo;
        TYPE_CHOICE_STRING = new String[]{
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[1])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[2])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[3])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[4])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[5])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[6])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[7])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[8])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[9])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[10])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[11])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[12])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[13]))
        };
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

                                                new EditDialog(itemView.getContext(), R.style.OrangeDialog, getLayoutPosition()).show();

//                                              new AlertDialog.Builder(itemView.getContext(), R.style.OrangeDialog).setItems(TYPE_CHOICE_STRING, new DialogInterface.OnClickListener() {
//                                                  @Override
//                                                  public void onClick(DialogInterface dialog, int which) {
//                                                      mUndoHistoryPresenter.editAtPosition(getLayoutPosition());
//                                                  }
//                                              }).show();
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


    public class EditDialog extends AlertDialog.Builder{

        private Spinner mTypeSpinner;
        private Spinner mPlayerSpinner;
        private int mType;
        private Player mPlayer;

        public EditDialog(@NonNull Context context, int themeResId, int position) {
            super(context, themeResId);

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_undo_edit, null);

            mTypeSpinner = view.findViewById(R.id.dialog_history_edit_type_spinner);
            mPlayerSpinner = view.findViewById(R.id.dialog_history_edit_player_spinner);

            mTypeSpinner.setAdapter(new SelectTypeAdapter());
            mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mType = (Integer)parent.getSelectedItem();
                    Log.d(TAG, "type = " + mType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mPlayerSpinner.setAdapter(new SelectPlayerAdapter(mGameInfo.getStartingPlayerList(), mGameInfo.getSubstitutePlayerList()));
            mPlayerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPlayer = (Player)parent.getSelectedItem();
                    Log.d(TAG, "name = " + mPlayer.getName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String number = mUndoList.get(position).getPlayer().getNumber();
            String type = context.getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(mUndoList.get(position).getType()));
            setTitle("變更選項");
            setMessage("\n將　"+number + "號  –  "+ type + "\n\n變更為：");
            setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick : type = " + mType);
                    Log.d(TAG, "name = " + mPlayer.getName());
                    dialog.cancel();
                }
            });
            setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            setView(view);
        }
    }
}
