package com.wadexhong.boxscore.gameboxscore.undohistory;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/10.
 */

public interface UndoHistoryContract {
    interface View extends BaseView<Presenter> {

        void setAdapter(UndoHistoryAdapter mAdapter);

        void updateUi();

        void scrollToPosition(int position);
    }

    interface Presenter extends BasePresenter {

        void createAdapter();

        void undoAtPosition(int position);

        void updateUi();

        void notifyInsert();

        void notifyRemove(int position);

        void notifyEdit(int position);

        void editUndoHistoryAtPosition(int position, Player player, int type);
    }
}
