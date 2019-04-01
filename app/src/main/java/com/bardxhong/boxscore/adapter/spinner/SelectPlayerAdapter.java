package com.bardxhong.boxscore.adapter.spinner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/6/4.
 */

public class SelectPlayerAdapter extends BaseAdapter {

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;

    public SelectPlayerAdapter(ArrayList<Player> starting, ArrayList<Player> substitute) {
        super();
        mStartingPlayerList = starting;
        mSubstitutePlayerList = substitute;
    }

    @Override
    public int getCount() {
        return mStartingPlayerList.size() + mSubstitutePlayerList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position > 4){
            return mSubstitutePlayerList.get(position - 5);
        }else {
            return mStartingPlayerList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_player, parent, false);
        TextView name = view.findViewById(R.id.item_select_playername_textview);
        TextView number = view.findViewById(R.id.item_select_playernumber_textview);
        if (position > 4){
            name.setText(mSubstitutePlayerList.get(position-5).getName());
            number.setText(mSubstitutePlayerList.get(position-5).getNumber());
        }else {
            name.setText(mStartingPlayerList.get(position).getName());
            number.setText(mStartingPlayerList.get(position).getNumber());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_player_dropdown, parent, false);
        TextView name = view.findViewById(R.id.item_select_playername_textview);
        TextView number = view.findViewById(R.id.item_select_playernumber_textview);
        if (position > 4){
            name.setText(mSubstitutePlayerList.get(position-5).getName());
            number.setText(mSubstitutePlayerList.get(position-5).getNumber());
        }else {
            name.setText(mStartingPlayerList.get(position).getName());
            number.setText(mStartingPlayerList.get(position).getNumber());
        }
        return view;
    }
}
