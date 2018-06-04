package com.wadexhong.boxscore.gameboxscore;

import android.util.SparseIntArray;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.dialogfragment.datastatistic.DataStatisticDialog;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Undo;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/3.
 */

public interface GameBoxScoreContract {
    interface View extends BaseView<Presenter> {

        void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter);

        void setInitDataOnScreen(SparseIntArray mTeamData);

        void updateUiTeamData();

        GameInfo getGameInfo();

        void popDataStatisticDialog(DataStatisticDialog dialog);

        void showToast(String message);

        void setGameInfoFromResume();

        void setGameInfoFromInput();
    }

    interface Presenter extends BasePresenter {

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

        LinkedList<Undo> getUndoList();

        void scrollUp(int mPointerCount);

        void scrollDown(int mPointerCount);

        void scrollLeft(int mPointerCount);

        void scrollRight(int mPointerCount);

        void checkIsResume(boolean mIsResume);

        GameInfo resumeGameInfo(GameInfo mGameInfo);

        void removeGameDataSharedPreferences();

        void removeGameDataInDataBase();

        void longPressOpponentTeamScore();

        void longPressOpponentTeamFoul();

        void longPressQuarter();

        void saveAndEndCurrentGame();

        void editAtPosition(int position);
    }
}
