package com.example.wade8.boxscore.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.SharedPreferenceHelper;

/**
 * Created by wade8 on 2018/5/25.
 */

public class SettingAdapter extends RecyclerView.Adapter {

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_GESTURE = 1;
    private String[] GESTURE_SETTING;
    private static final String[] GESTURE_TYPE_ARRAY
              = new String[]{
              SharedPreferenceHelper.DOUBLE_UP,
              SharedPreferenceHelper.DOUBLE_DOWN,
              SharedPreferenceHelper.DOUBLE_LEFT,
              SharedPreferenceHelper.DOUBLE_RIGHT,
              SharedPreferenceHelper.TRIPLE_UP,
              SharedPreferenceHelper.TRIPLE_DOWN,
              SharedPreferenceHelper.TRIPLE_LEFT,
              SharedPreferenceHelper.TRIPLE_RIGHT
    };

    private int[] TYPE_CHOICE_INT;
    private String[] TYPE_CHOICE_STRING;


    public SettingAdapter() {

        TYPE_CHOICE_INT = new int[]{-1,
                  Constants.RecordDataType.TWO_POINT_SHOT_MADE,
                  Constants.RecordDataType.TWO_POINT_SHOT_MISSED,
                  Constants.RecordDataType.THREE_POINT_SHOT_MADE,
                  Constants.RecordDataType.THREE_POINT_SHOT_MISSED,
                  Constants.RecordDataType.FREE_THROW_SHOT_MADE,
                  Constants.RecordDataType.FREE_THROW_SHOT_MISSED,
                  Constants.RecordDataType.OFFENSIVE_REBOUND,
                  Constants.RecordDataType.DEFENSIVE_REBOUND,
                  Constants.RecordDataType.ASSIST,
                  Constants.RecordDataType.STEAL,
                  Constants.RecordDataType.BLOCK,
                  Constants.RecordDataType.FOUL,
                  Constants.RecordDataType.TURNOVER};

        TYPE_CHOICE_STRING = new String[]{
                  BoxScore.getAppContext().getResources().getString(R.string.none),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[1])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[2])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[3])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[4])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[5])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[6])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[7])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[8])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[9])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[10])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[11])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[12])),
                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[13]))
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_others, parent, false));
        }else {
            return new GestureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_gesture_child, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            ((HeaderViewHolder)holder).bind();
        }else {
            ((GestureViewHolder)holder).bind(position-1);
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{


        public HeaderViewHolder(View itemView) {
            super(itemView);

        }

        private void bind() {
        }
    }

    public class GestureViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout mLayout;
        private TextView mGestureDirection;
        private TextView mGestureDataType;


        public GestureViewHolder(View itemView) {
            super(itemView);

            GESTURE_SETTING = itemView.getResources().getStringArray(R.array.gesture_setting);
            mGestureDirection = itemView.findViewById(R.id.item_setting_gesture_text);
            mGestureDataType = itemView.findViewById(R.id.item_setting_gesture_type);
            mLayout = itemView.findViewById(R.id.item_setting_gesture_child_layout);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext(),R.style.OrangeDialog)
                              .setTitle(GESTURE_SETTING[getLayoutPosition()-1])
                              .setItems(TYPE_CHOICE_STRING, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      mGestureDataType.setText(BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(TYPE_CHOICE_INT[which],R.string.none)));
                                      SharedPreferenceHelper.write(GESTURE_TYPE_ARRAY[getLayoutPosition()-1],TYPE_CHOICE_INT[which]);
                                  }
                              }).show();
                }
            });
        }

        private void bind(int position) {
            mGestureDirection.setText(GESTURE_SETTING[position]);
            int value = SharedPreferenceHelper.read(GESTURE_TYPE_ARRAY[position],-1);
            if (value == -1){
                mGestureDataType.setText(R.string.none);
            }else {
                mGestureDataType.setText(Constants.TITLE_SPARSE_ARRAY.get(value));
            }

        }
    }
}
