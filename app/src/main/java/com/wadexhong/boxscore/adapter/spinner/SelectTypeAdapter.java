package com.wadexhong.boxscore.adapter.spinner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wade8 on 2018/6/4.
 */

public class SelectTypeAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return Constants.TYPE_CHOICE_INT.length - 1;
    }

    @Override
    public Object getItem(int position) {
        return Constants.TYPE_CHOICE_INT [position + 1];
    }

    @Override
    public long getItemId(int position) {
        return Constants.TYPE_CHOICE_INT [position + 1];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_type, parent, false);
        TextView mTextView = view.findViewById(R.id.item_select_type_textview);
        mTextView.setText(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[position+1]));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_type_dropdown, parent, false);
        TextView mTextView = view.findViewById(R.id.item_select_type_textview);
        mTextView.setText(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[position+1]));

        return view;
    }
}
