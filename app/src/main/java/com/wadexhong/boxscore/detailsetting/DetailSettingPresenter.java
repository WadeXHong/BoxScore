package com.wadexhong.boxscore.detailsetting;

import android.text.TextUtils;

import com.wadexhong.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public class DetailSettingPresenter implements DetailSettingContract.Presenter{

    private static final String TAG = DetailSettingPresenter.class.getSimpleName();

    private final DetailSettingContract.View mDetailSettingView;

    public DetailSettingPresenter(DetailSettingContract.View detailSettingView) {
        this.mDetailSettingView = detailSettingView;
        detailSettingView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void maxFoulPlus(String input) {

        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber<9){
                mDetailSettingView.updateMaxFoulInEditText(String.valueOf(inputNumber+1));
            }else {
                mDetailSettingView.showToast("犯規次數應小於10");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void maxFoulMinus(String input) {

        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber>1){
                mDetailSettingView.updateMaxFoulInEditText(String.valueOf(inputNumber-1));
            }else {
                mDetailSettingView.showToast("犯規次數應大於0");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void quarterLengthPlus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber<40){
                mDetailSettingView.updateQuarterLengthEditText(String.valueOf(inputNumber+1));
            }else {
                mDetailSettingView.showToast("單節時間最大值為40分鐘");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void quarterLengthMinus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber>1){
                mDetailSettingView.updateQuarterLengthEditText(String.valueOf(inputNumber-1));
            }else {
                mDetailSettingView.showToast("單節時間應大於0分鐘");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void totalQuarterPlus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber<4){
                mDetailSettingView.updateTotalQuarterEditText(String.valueOf(inputNumber+1));
            }else {
                mDetailSettingView.showToast("總節數最大為4節");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void totalQuarterMinus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber>1){
                mDetailSettingView.updateTotalQuarterEditText(String.valueOf(inputNumber-1));
            }else {
                mDetailSettingView.showToast("總節數應大於0");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void timeoutsFirstHalfPlus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber<9){
                mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber+1));
            }else {
                mDetailSettingView.showToast("上半場最大暫停次數為9次");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void timeoutsFirstHalfMinus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber>1){
                mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber-1));
            }else {
                mDetailSettingView.showToast("上半場暫停次數應大於0");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void timeoutsSecondHalfPlus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber<9){
                mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber+1));
            }else {
                mDetailSettingView.showToast("下半場最大暫停次數為9次");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    @Override
    public void timeoutsSecondHalfMinus(String input) {
        if(checkIsLegal(input)){
            int inputNumber = Integer.parseInt(input);
            if (inputNumber>1){
                mDetailSettingView.updateTimeoutsSecondHalfEditText(String.valueOf(inputNumber-1));
            }else {
                mDetailSettingView.showToast("下半場暫停次數應大於0");
            }
        }else {
            mDetailSettingView.showToast("格式錯誤");
        }
    }

    private boolean checkIsLegal(String input){
        return TextUtils.isDigitsOnly(input) && !TextUtils.isEmpty(input);
    }

    public void getDataFromView(GameInfo gameInfo) {
        mDetailSettingView.getSettingResult(gameInfo);
    }
}
