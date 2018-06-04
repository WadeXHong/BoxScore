package com.wadexhong.boxscore.adapter.spinner;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/6/4.
 */

public class SelectPlayerAdapter extends BaseAdapter {

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;

    public SelectPlayerAdapter() {
        super();

    }

    @Override
    public int getCount() {
        return Constants.TYPE_CHOICE_INT.length - 1;
    }

    @Override
    public Object getItem(int position) {
        return Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT [position + 1]);
    }

    @Override
    public long getItemId(int position) {
        return Constants.TYPE_CHOICE_INT [position + 1];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
