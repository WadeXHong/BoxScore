package com.example.wade8.boxscore.gamenamesetting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.playerlist.PlayerListFragment;


public class GameNameSettingFragment extends Fragment implements GameNameSettingContract.View{

    private static final String TAG = GameNameSettingFragment.class.getSimpleName();

    private GameNameSettingContract.Presenter mPresenter;

    private EditText mGameTitle;
    private EditText mOpponent;
    private EditText mGameDate;
    private Spinner mYourTeam;


    public GameNameSettingFragment() {
        // Required empty public constructor
    }

    public static GameNameSettingFragment newInstance(){
        return new GameNameSettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_name_setting,container,false);

        mGameTitle = view.findViewById(R.id.fragment_gamenamesetting_gametitle_edittext);
        mGameDate = view.findViewById(R.id.fragment_gamenamesetting_gamedate_edittext);
        mOpponent = view.findViewById(R.id.fragment_gamenamesetting_opponent_edittext);
        mYourTeam = view.findViewById(R.id.fragment_gamenamesetting_yourteam_spinner);

        return view;
    }


    @Override
    public void setPresenter(GameNameSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
