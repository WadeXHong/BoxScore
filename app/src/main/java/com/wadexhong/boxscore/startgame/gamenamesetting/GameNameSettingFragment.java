package com.wadexhong.boxscore.startgame.gamenamesetting;

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
import com.wadexhong.boxscore.adapter.spinner.SelectTeamAdapter;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.TeamInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GameNameSettingFragment extends Fragment implements GameNameSettingContract.View {

    private static String TAG = GameNameSettingFragment.class.getSimpleName();

    private GameNameSettingContract.Presenter mPresenter;

    private EditText mGameTitleEditText;
    private EditText mOpponentEditText;
    private TextView mGameDateTextView;
    private Spinner mYourTeamSpinner;

    public GameNameSettingFragment() {
        // Required empty public constructor
    }

    public static GameNameSettingFragment newInstance() {
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

        mGameTitleEditText = view.findViewById(R.id.fragment_gamenamesetting_gametitle_edittext);
        mGameDateTextView = view.findViewById(R.id.fragment_gamenamesetting_gamedate_edittext);
        mOpponentEditText = view.findViewById(R.id.fragment_gamenamesetting_opponent_edittext);
        mYourTeamSpinner = view.findViewById(R.id.fragment_gamenamesetting_yourteam_spinner);
        mYourTeamSpinner.setAdapter(new SelectTeamAdapter(mPresenter.getTeamInfos()));

        final SimpleDateFormat format = new SimpleDateFormat(getString(R.string.simple_date_format));
        final Calendar calendar = Calendar.getInstance();
        mGameDateTextView.setText(format.format(calendar.getTime()));
        setListenerOnView(format, calendar);

        return view;
    }

    private void setListenerOnView(final SimpleDateFormat format, final Calendar calendar) {

        mGameDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), R.style.OrangeDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        String date = format.format(calendar.getTime());
                        mGameDateTextView.setText(date);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mOpponentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0 && s.charAt(0) == '\u0020') s.delete(0, 1);
                if (s.toString().trim().equals("")) {
                    mOpponentEditText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename_illegal, null));
                } else {
                    mOpponentEditText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename, null));
                }
            }
        });

        mGameTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(0) == '\u0020') s.delete(0, 1);
            }
        });

        mYourTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setDefaultPlayerList(((TeamInfo) parent.getSelectedItem()).getTeamId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public String[] getCheckedInput() {
        return new String[]{mOpponentEditText.getText().toString()};//未來新增條件可加在這裡
    }

    @Override
    public void getSettingResult(GameInfo gameInfo) {

        gameInfo.setGameName(mGameTitleEditText.getText().toString());
        gameInfo.setOpponentName(mOpponentEditText.getText().toString());
        gameInfo.setGameDate(mGameDateTextView.getText().toString());

        if (mYourTeamSpinner.getSelectedItem() != null) {
            gameInfo.setYourTeam(((TeamInfo) mYourTeamSpinner.getSelectedItem()).getTeamName());
            gameInfo.setYourTeamId(((TeamInfo) mYourTeamSpinner.getSelectedItem()).getTeamId());
        } else {
            gameInfo.setYourTeam(getString(R.string.quick_game_team_name));
            gameInfo.setYourTeamId(getString(R.string.quick_game_team_id));
        }
    }

    @Override
    public void setPresenter(GameNameSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
