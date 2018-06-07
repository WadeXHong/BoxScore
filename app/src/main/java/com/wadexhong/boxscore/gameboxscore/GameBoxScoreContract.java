package com.wadexhong.boxscore.gameboxscore;

import android.util.SparseIntArray;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.adapter.ViewPagerFragmentAdapter;
import com.wadexhong.boxscore.gameboxscore.datastatistic.DataStatisticDialog;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
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

        GameInfo getGameInfo();

        LinkedList<Undo> getUndoList();

        void scrollUp(int mPointerCount);

        void scrollDown(int mPointerCount);

        void scrollLeft(int mPointerCount);

        void scrollRight(int mPointerCount);

        void pressYourTeamFoul();

        void pressOpponentTeamFoul();

        void pressQuarter();

        void pressOpponentTeamScore();

        void pressUndo();

        void pressDataStatistic();

        void longPressOpponentTeamFoul();

        void longPressOpponentTeamScore();

        void writeInitDataIntoModel();

        void undoDataInDb(int position);

        void updateUi();

        void editDataInDb(Player player, int type);

        void checkIsResume(boolean mIsResume);

        GameInfo resumeGameInfo(GameInfo mGameInfo);

        void removeGameDataSharedPreferences();

        void removeGameDataInDataBase();

        void longPressQuarter();

        void saveAndEndCurrentGame();

        void editAtPosition(int position, Player player, int type);
    }
}
