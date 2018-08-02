package com.wadexhong.boxscore.adapter.spinner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/7/31.
 */

public class SelectQuarterAdapter extends BaseAdapter {

    private int mThisQuarter;
    private int[] mQuarterList;

    public SelectQuarterAdapter(int thisQuarter) {
        super();
        mThisQuarter = thisQuarter;
        mQuarterList = new int[thisQuarter - 1];
        for (int i = 0; i < thisQuarter - 1; i++) {
            mQuarterList[i] = i + 1;
        }
    }

    @Override
    public int getCount() {
        return mThisQuarter - 1;
    }

    @Override
    public Object getItem(int position) {
        return mQuarterList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_type, parent, false);
        TextView mTextView = view.findViewById(R.id.item_select_type_textview);
        mTextView.setText(mQuarterList[position] + "");

        return view;
    }
}
