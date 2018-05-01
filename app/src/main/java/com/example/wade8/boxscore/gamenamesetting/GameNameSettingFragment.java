package com.example.wade8.boxscore.gamenamesetting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.playerlist.PlayerListFragment;


public class GameNameSettingFragment extends Fragment {



    public GameNameSettingFragment() {
        // Required empty public constructor
    }

    public static GameNameSettingFragment newInstance(){
        return new GameNameSettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_name_setting,container,false);

        return view;
    }


}
