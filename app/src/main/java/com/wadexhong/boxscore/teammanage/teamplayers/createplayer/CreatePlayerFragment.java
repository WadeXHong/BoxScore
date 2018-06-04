package com.wadexhong.boxscore.teammanage.teamplayers.createplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.Player;

public class CreatePlayerFragment extends Fragment implements CreatePlayerContract.View{

    private static final String TAG = CreatePlayerFragment.class.getSimpleName();
    public static final int POSITION_C = 0;
    public static final int POSITION_PF = 1;
    public static final int POSITION_SF = 2;
    public static final int POSITION_SG = 3;
    public static final int POSITION_PG = 4;

    private CreatePlayerContract.Presenter mPresenter;

    private EditText mPlayerName;
    private EditText mPlayerNumber;
    private CheckBox mC;
    private CheckBox mPF;
    private CheckBox mSF;
    private CheckBox mSG;
    private CheckBox mPG;
    private Button mConfirm;
    private int[] mCheckBoxStatus = {0,0,0,0,0};

    private String mTeamId;
    private boolean isNameLegal;
    private boolean isNumberLegal;

    private View.OnClickListener mOnCheckboxClickedListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePlayerFragment() {
        // Required empty public constructor
    }

    public static CreatePlayerFragment newInstance() {
        CreatePlayerFragment fragment = new CreatePlayerFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        mOnCheckboxClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_player, container, false);

        mPlayerName = view.findViewById(R.id.fragment_createplayer_playername_edittext);
        mPlayerNumber = view.findViewById(R.id.fragment_createplayer_playernumber_edittext);
        mC = view.findViewById(R.id.fragment_createplayer_position_c);
        mPF = view.findViewById(R.id.fragment_createplayer_position_pf);
        mSF = view.findViewById(R.id.fragment_createplayer_position_sf);
        mSG = view.findViewById(R.id.fragment_createplayer_position_sg);
        mPG = view.findViewById(R.id.fragment_createplayer_position_pg);
        mConfirm = view.findViewById(R.id.fragment_createplayer_createbutton);

        mPlayerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0 && s.charAt(0) == '\u0020') s.delete(0,1);
                if (s.toString().trim().equals("")){ //等於空白
                    isNameLegal = false;
                    mPlayerName.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename_orange, null));
                }else {
                    isNameLegal = true;
                    mPlayerName.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename, null));
                }
            }
        });

        mPlayerNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2 && s.charAt(0) == '\u0030') s.delete(0,1);

                if (s.toString().trim().equals("")){ //輸入不符
                    isNumberLegal = false;
                    mPlayerNumber.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename_orange, null));
                }else if (mPresenter.checkNumberIsExist(mTeamId, s.toString())) { //背號是否存在
                    isNumberLegal = false;
                    mPlayerNumber.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename_orange, null));
                    Toast.makeText(getContext(),"此背號已被使用", Toast.LENGTH_SHORT).show();
                }else {
                    isNumberLegal = true;
                    mPlayerNumber.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_edittext_gamename, null));
                }
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameLegal && isNumberLegal) {
                    String playerNumber = mPlayerNumber.getText().toString();
                    String playerName = mPlayerName.getText().toString();
                    mPlayerName.setText("");
                    mPlayerNumber.setText("");
                    isNumberLegal = isNameLegal = false;
                    mPresenter.createPlayer(mTeamId, new Player(playerNumber, playerName, mCheckBoxStatus));

                }

            }
        });

        mC.setOnClickListener(mOnCheckboxClickedListener);
        mPF.setOnClickListener(mOnCheckboxClickedListener);
        mSF.setOnClickListener(mOnCheckboxClickedListener);
        mSG.setOnClickListener(mOnCheckboxClickedListener);
        mPG.setOnClickListener(mOnCheckboxClickedListener);


        mPresenter.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.refreshToolBar();
    }

    private void onCheckboxClicked(View view){
        Log.d(TAG,"mOnCheckboxClickedListener executed");
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.fragment_createplayer_position_c:
                mCheckBoxStatus[POSITION_C] = checked ? 1 : 0 ;
                break;
            case R.id.fragment_createplayer_position_pf:
                mCheckBoxStatus[POSITION_PF] = checked ? 1 : 0 ;
                break;
            case R.id.fragment_createplayer_position_sf:
                mCheckBoxStatus[POSITION_SF] = checked ? 1 : 0 ;
                break;
            case R.id.fragment_createplayer_position_sg:
                mCheckBoxStatus[POSITION_SG] = checked ? 1 : 0 ;
                break;
            case R.id.fragment_createplayer_position_pg:
                mCheckBoxStatus[POSITION_PG] = checked ? 1 : 0 ;
                break;
        }
    }

    @Override
    public void setPresenter(CreatePlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }


    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }
}
