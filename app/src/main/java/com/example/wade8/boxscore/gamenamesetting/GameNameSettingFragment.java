package com.example.wade8.boxscore.gamenamesetting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.playerlist.PlayerListFragment;

import java.util.Date;


public class GameNameSettingFragment extends Fragment implements GameNameSettingContract.View{

    private static final String TAG = GameNameSettingFragment.class.getSimpleName();

    private GameNameSettingContract.Presenter mPresenter;

    private EditText mGameTitle;
    private EditText mOpponent;
    private TextView mGameDate;
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

        mGameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                java.util.Calendar c = java.util.Calendar.getInstance();
                new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"年"+(month+1)+"月"+dayOfMonth+"日";
                        mGameDate.setText(date);
                    }
                }, c.get(java.util.Calendar.YEAR),c.get(java.util.Calendar.MONTH),c.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });
        return view;
    }


    @Override
    public void setPresenter(GameNameSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {
        gameInfo.setGameName(mGameTitle.getText().toString());
        gameInfo.setOpponentName(mOpponent.getText().toString());
        gameInfo.setGameDate(mGameDate.getText().toString());
        //TODO spinner data
//        gameInfo.setYourTeam(mYourTeam.getSelectedItem().toString());
    }
}
