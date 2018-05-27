package com.wadexhong.boxscore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.TeamInfo;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/22.
 */

public class SelectTeamAdapter extends BaseAdapter{

    ArrayList<TeamInfo> mTeamInfos;

    public SelectTeamAdapter(ArrayList<TeamInfo> teamInfos) {
        mTeamInfos = teamInfos;
    }

    @Override
    public int getCount() {
        return mTeamInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeamInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_team, parent, false);
        TextView mTextView = view.findViewById(R.id.item_select_team_textview);
        mTextView.setText(mTeamInfos.get(position).getTeamName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_team_dropdown, parent, false);
        TextView mTextView = view.findViewById(R.id.item_select_team_textview);
        mTextView.setText(mTeamInfos.get(position).getTeamName());

        return view;
    }
}
