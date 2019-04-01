package com.bardxhong.boxscore.gameboxscore.undohistory;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;
import com.bardxhong.boxscore.objects.Player;

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
