package com.example.wade8.boxscore.gameboxscore;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.ViewPagerFragmentAdapter;
import com.example.wade8.boxscore.dialogfragment.datastatistic.DataStatisticDialog;
import com.example.wade8.boxscore.objects.GameInfo;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface GameBoxScoreContract {
    interface View extends BaseView<Presenter>{

        void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter);

        void setInitDataOnScreen(SparseIntArray mTeamData);

        void updateUiTeamData();

        GameInfo getGameInfo();

        void popDataStatisticDialog(DataStatisticDialog dialog);

        void showToast(String message);
    }

    interface Presenter extends BasePresenter{

        void writeInitDataIntoModel();

        void pressYourTeamFoul();

        void pressOpponentTeamFoul();

        void pressQuarter();

        void pressOpponentTeamScore();

        void pressUndo();

        void undoDataInDb(int position);

        GameInfo getGameInfo();

        void pressDataStatistic();

        void updateUi();

        void editDataInDb(int position, int type);
    }
}
