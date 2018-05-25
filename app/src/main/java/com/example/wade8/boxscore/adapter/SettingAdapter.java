package com.example.wade8.boxscore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.R;

/**
 * Created by wade8 on 2018/5/25.
 */

public class SettingAdapter extends RecyclerView.Adapter {

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_GESTURE = 1;
    private String[] GESTURE_SETTING;

    public SettingAdapter() {
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

        private TextView mTextView;


        public GestureViewHolder(View itemView) {
            super(itemView);
            GESTURE_SETTING = itemView.getResources().getStringArray(R.array.gesture_setting);
            mTextView = itemView.findViewById(R.id.item_setting_gesture_text);
        }

        private void bind(int position) {
            mTextView.setText(GESTURE_SETTING[position]);
        }
    }
}
