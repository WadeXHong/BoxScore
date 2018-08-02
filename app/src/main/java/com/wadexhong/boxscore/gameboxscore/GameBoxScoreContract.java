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

        GameInfo getGameInfo();

        void setGameInfoFromResume();

        void setGameInfoFromInput();

        void setViewPagerAdapter(ViewPagerFragmentAdapter mViewPagerFragmentAdapter);

        void setInitDataOnScreen(SparseIntArray mTeamData);

        void updateUiTeamData();

        void popDataStatisticDialog(DataStatisticDialog dialog);

        void showToast(String message);
    }

    interface Presenter extends BasePresenter {

        GameInfo getGameInfo();

        LinkedList<Undo> getUndoList();

        void writeInitDataIntoModel();

        void checkIsResume(boolean mIsResume);

        GameInfo resumeGameInfo(GameInfo mGameInfo);

        void removeGameDataSharedPreferences();

        void removeGameDataInDataBase();

        void saveAndEndCurrentGame();

        void undoDataInDb(int position);

        void editDataInDb(Player player, int type);

        void editUndoHistoryAtPosition(int position, Player player, int type);

        void pressYourTeamFoul();

        void pressOpponentTeamFoul();

        void pressQuarter();

        void pressOpponentTeamScore();

        void pressUndo();

        void pressMakeUp(Player player, int type, int quarter);

        void pressDataStatistic();

        void longPressOpponentTeamFoul();

        void longPressOpponentTeamScore();

        void longPressQuarter();

        void scrollUp(int mPointerCount);

        void scrollDown(int mPointerCount);

        void scrollLeft(int mPointerCount);

        void scrollRight(int mPointerCount);

        void updateUi();
    }
}
