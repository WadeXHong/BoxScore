package com.example.wade8.boxscore.playerlist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;


public class PlayerListFragment extends Fragment {



    public PlayerListFragment() {
        // Required empty public constructor
    }

    public static PlayerListFragment newInstance(){
        return new PlayerListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playerlist, container, false);
    }

}
