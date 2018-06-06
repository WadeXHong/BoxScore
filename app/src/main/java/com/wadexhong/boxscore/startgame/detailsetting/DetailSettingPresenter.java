package com.wadexhong.boxscore.startgame.detailsetting;

import android.text.TextUtils;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/1.
 */

public class DetailSettingPresenter implements DetailSettingContract.Presenter {

    private final String TAG = DetailSettingPresenter.class.getSimpleName();

    private final int MAX_FOUL = 9;
    private final int MIN_FOUL = 1;
    private final int MAX_QUARTER_LENGTH = 40;
    private final int MIN_QUARTER_LENGTH = 1;
    private final int MAX_TOTAL_QUARTER = 4;
    private final int MIN_TOTAL_QUARTER = 1;
    private final int MAX_TIMEOUT_FIRST_HALF = 9;
    private final int MIN_TIMEOUT_FIRST_HALF = 1;
    private final int MAX_TIMEOUT_SECOND_HALF = 9;
    private final int MIN_TIMEOUT_SECOND_HALF = 1;

    private final DetailSettingContract.View mDetailSettingView;

    public DetailSettingPresenter(DetailSettingContract.View detailSettingView) {
        this.mDetailSettingView = detailSettingView;
        detailSettingView.setPresenter(this);
    }

    @Override
    public void maxFoulPlus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber < MAX_FOUL) {
            mDetailSettingView.updateMaxFoulInEditText(String.valueOf(inputNumber + 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.max_foul_message, MAX_FOUL));
        }
    }

    @Override
    public void maxFoulMinus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber > MIN_FOUL) {
            mDetailSettingView.updateMaxFoulInEditText(String.valueOf(inputNumber - 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.min_foul_message, MIN_FOUL));
        }
    }

    @Override
    public void quarterLengthPlus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber < MAX_QUARTER_LENGTH) {
            mDetailSettingView.updateQuarterLengthEditText(String.valueOf(inputNumber + 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.max_quarter_length_message, MAX_QUARTER_LENGTH));
        }
    }

    @Override
    public void quarterLengthMinus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber > MIN_QUARTER_LENGTH) {
            mDetailSettingView.updateQuarterLengthEditText(String.valueOf(inputNumber - 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.min_quarter_length_message, MIN_QUARTER_LENGTH));
        }
    }

    @Override
    public void totalQuarterPlus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber < MAX_TOTAL_QUARTER) {
            mDetailSettingView.updateTotalQuarterEditText(String.valueOf(inputNumber + 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.max_total_quarter_message, MAX_TOTAL_QUARTER));
        }
    }

    @Override
    public void totalQuarterMinus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber > MIN_TOTAL_QUARTER) {
            mDetailSettingView.updateTotalQuarterEditText(String.valueOf(inputNumber - 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.min_total_quarter_message, MIN_TOTAL_QUARTER));
        }
    }

    @Override
    public void timeoutsFirstHalfPlus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber < MAX_TIMEOUT_FIRST_HALF) {
            mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber + 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.max_timeout_first_half, MAX_TIMEOUT_FIRST_HALF));
        }
    }

    @Override
    public void timeoutsFirstHalfMinus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber > MIN_TIMEOUT_FIRST_HALF) {
            mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber - 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.min_timeout_first_half, MIN_TIMEOUT_FIRST_HALF));
        }
    }

    @Override
    public void timeoutsSecondHalfPlus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber < MAX_TIMEOUT_SECOND_HALF) {
            mDetailSettingView.updateTimeoutsFirstHalfEditText(String.valueOf(inputNumber + 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.max_timeout_second_half, MAX_TIMEOUT_SECOND_HALF));
        }
    }

    @Override
    public void timeoutsSecondHalfMinus(String input) {

        int inputNumber = Integer.parseInt(input);

        if (inputNumber > MIN_TIMEOUT_SECOND_HALF) {
            mDetailSettingView.updateTimeoutsSecondHalfEditText(String.valueOf(inputNumber - 1));
        } else {
            mDetailSettingView.showToast(BoxScore.getStringEasy(R.string.min_timeout_second_half, MIN_TIMEOUT_SECOND_HALF));
        }
    }

    public void getDataFromView(GameInfo gameInfo) {
        mDetailSettingView.getSettingResult(gameInfo);
    }

    @Override
    public void start() {

    }
}
