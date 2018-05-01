package com.example.wade8.boxscore.detailsetting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.gamenamesetting.GameNameSettingFragment;


public class DetailSettingFragment extends Fragment {


    public DetailSettingFragment() {
        // Required empty public constructor
    }

    public static DetailSettingFragment newInstance(){
        return new DetailSettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_setting, container, false);
    }

}
