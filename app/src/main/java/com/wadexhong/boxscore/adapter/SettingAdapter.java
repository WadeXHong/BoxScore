package com.wadexhong.boxscore.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.SharedPreferenceHelper;
import com.wadexhong.boxscore.setting.SettingContract;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/25.
 */

public class SettingAdapter extends RecyclerView.Adapter {

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_GESTURE = 1;
    private static final int VIEWTYPE_OTHERS = 2;
    private static final String[] GESTURE_TYPE_ARRAY
              = new String[]{
              SharedPreferenceHelper.DOUBLE_UP,
              SharedPreferenceHelper.DOUBLE_DOWN,
              SharedPreferenceHelper.DOUBLE_LEFT,
              SharedPreferenceHelper.DOUBLE_RIGHT,
              SharedPreferenceHelper.TRIPLE_UP,
              SharedPreferenceHelper.TRIPLE_DOWN,
              SharedPreferenceHelper.TRIPLE_LEFT,
              SharedPreferenceHelper.TRIPLE_RIGHT
    };

    private SettingContract.Presenter mSettingPresenter;

//    private String[] TYPE_CHOICE_STRING;
    private String[] GESTURE_SETTING;



    public SettingAdapter(SettingContract.Presenter presenter) {
        mSettingPresenter = presenter;

//        TYPE_CHOICE_STRING = new String[]{
//                  BoxScore.getAppContext().getResources().getString(R.string.none),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[1])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[2])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[3])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[4])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[5])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[6])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[7])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[8])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[9])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[10])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[11])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[12])),
//                  BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[13]))
//        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)return 0;
        if (position == getItemCount()-1)return 2;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_header, parent, false));
        }else if (viewType == VIEWTYPE_OTHERS) {
            return new OtherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_others, parent, false));
        }else {
            return new GestureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_gesture_child, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            ((HeaderViewHolder)holder).bind();
        }else if (position == getItemCount()-1){
            ((OtherViewHolder)holder).bind();
        }else {
            ((GestureViewHolder)holder).bind(position-1);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private SeekBar mSeekBar;
        private Switch mSwitch;


        public HeaderViewHolder(final View itemView) {
            super(itemView);

            mSwitch = itemView.findViewById(R.id.item_setting_header_switch);
            mSeekBar = itemView.findViewById(R.id.item_setting_header_seekbar);

            if (BoxScore.sBrightness == -1){
                mSwitch.setChecked(false);
                mSeekBar.setEnabled(false);
            }else {
                mSwitch.setChecked(true);
                mSeekBar.setProgress((int)BoxScore.sBrightness*100);
            }

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        mSeekBar.setEnabled(true);
                        mSeekBar.setProgress(50);
                        mSettingPresenter.setBrightness(50);
                    }else {
                        mSeekBar.setEnabled(false);
                        mSettingPresenter.setBrightness(-1f);
                    }
                }
            });

            int brightness = (int)(SharedPreferenceHelper.read(SharedPreferenceHelper.BRIGHTNESS, -0.01f)*100);
            if (brightness == -1) {
                mSeekBar.setProgress(50);
            }else {
                mSeekBar.setProgress(brightness);
            }

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    float brightness = (float)progress/100;
                    SharedPreferenceHelper.write(SharedPreferenceHelper.BRIGHTNESS,brightness);
                    if (SharedPreferenceHelper.read(SharedPreferenceHelper.BRIGHTNESS,-1f) != -1f)
                        mSettingPresenter.setBrightness(brightness);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        private void bind() {
        }
    }

    public class GestureViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout mLayout;
        private TextView mGestureDirection;
        private TextView mGestureDataType;


        public GestureViewHolder(View itemView) {
            super(itemView);

            GESTURE_SETTING = itemView.getResources().getStringArray(R.array.gesture_setting);
            mGestureDirection = itemView.findViewById(R.id.item_setting_gesture_text);
            mGestureDataType = itemView.findViewById(R.id.item_setting_gesture_type);
            mLayout = itemView.findViewById(R.id.item_setting_gesture_child_layout);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext(),R.style.OrangeDialog)
                              .setTitle(GESTURE_SETTING[getLayoutPosition()-1])
                              .setItems(Constants.TYPE_CHOICE_STRING, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      mGestureDataType.setText(BoxScore.getAppContext().getResources().getString(Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[which],R.string.none)));
                                      SharedPreferenceHelper.write(GESTURE_TYPE_ARRAY[getLayoutPosition()-1], Constants.TYPE_CHOICE_INT[which]);
                                  }
                              }).show();
                }
            });
        }

        private void bind(int position) {
            mGestureDirection.setText(GESTURE_SETTING[position]);
            int value = SharedPreferenceHelper.read(GESTURE_TYPE_ARRAY[position],-1);
            if (value == -1){
                mGestureDataType.setText(R.string.none);
            }else {
                mGestureDataType.setText(Constants.TITLE_SPARSE_ARRAY.get(value));
            }

        }
    }


    public class OtherViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout mConstraintLayout;

        public OtherViewHolder(View itemView) {
            super(itemView);

            mConstraintLayout = itemView.findViewById(R.id.item_setting_account_logout);
            mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPresenter.signOut();
                    FirebaseAuth.getInstance().signOut();
                }
            });
        }


        private void bind() {

        }
    }
}
