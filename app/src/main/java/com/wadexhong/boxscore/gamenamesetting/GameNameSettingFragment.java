package com.wadexhong.boxscore.gamenamesetting;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.SelectTeamAdapter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.TeamInfo;

import java.util.Calendar;


public class GameNameSettingFragment extends Fragment implements GameNameSettingContract.View{

    private static final String TAG = GameNameSettingFragment.class.getSimpleName();

    private GameNameSettingContract.Presenter mPresenter;

    private EditText mGameTitle;
    private EditText mOpponent;
    private TextView mGameDate;
    private Spinner mYourTeam;
    private SelectTeamAdapter mAdapter;


    public GameNameSettingFragment() {
        // Required empty public constructor
    }

    public static GameNameSettingFragment newInstance(){
        return new GameNameSettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_name_setting, container, false);

        mGameTitle = view.findViewById(R.id.fragment_gamenamesetting_gametitle_edittext);
        mGameDate = view.findViewById(R.id.fragment_gamenamesetting_gamedate_edittext);
        mOpponent = view.findViewById(R.id.fragment_gamenamesetting_opponent_edittext);
        mYourTeam = view.findViewById(R.id.fragment_gamenamesetting_yourteam_spinner);
        String year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)  + "年";
        String month = (java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1) < 10 ? "0" +(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1) +"月" : (java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1) +"月";
        String day = java.util.Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10 ? "0" + java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH) +"日" : java.util.Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +"日";
        String today =  year + month + day;
        mGameDate.setText(today);
        mGameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                java.util.Calendar c = java.util.Calendar.getInstance();
                new DatePickerDialog(getContext(), R.style.OrangeDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthWithZero = (month+1) < 10 ? String.valueOf("0"+(month+1)) : String.valueOf(month+1);
                        String dayOfMonthWithZero = dayOfMonth < 10 ? String.valueOf("0"+dayOfMonth) : String.valueOf(dayOfMonth);
                        String date = year + "年" + monthWithZero + "月" + dayOfMonthWithZero + "日";
                        mGameDate.setText(date);
                    }
                }, c.get(java.util.Calendar.YEAR), c.get(java.util.Calendar.MONTH), c.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });
        mOpponent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0 && s.charAt(0) == '\u0020') s.delete(0,1);
                if (s.toString().trim().equals("")) {
                    mOpponent.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename_illegal, null));
                } else {
                    mOpponent.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename, null));
                }
            }
        });

        mGameTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(0) == '\u0020') s.delete(0,1);
            }
        });

        mAdapter = new SelectTeamAdapter(mPresenter.getTeamInfos());
        mYourTeam.setAdapter(mAdapter);
        mYourTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setDefaultPlayerList(((TeamInfo)parent.getSelectedItem()).getTeamId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        gameInfo.setYourTeam(((TeamInfo)mYourTeam.getSelectedItem()).getTeamName());
    }

    @Override
    public String[] getCheckedInput() {
        return new String[]{mOpponent.getText().toString()};//未來新增條件可加在這裡
    }
}
