package com.wadexhong.boxscore.undohistory;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;
import com.wadexhong.boxscore.adapter.UndoHistoryAdapter;

/**
 * Created by wade8 on 2018/5/10.
 */

public interface UndoHistoryContract {
    interface View extends BaseView<Presenter> {

        void setAdapter(UndoHistoryAdapter mAdapter);

        void updateUi();
    }

    interface Presenter extends BasePresenter {

        void createAdapter();

        void undoAtPosition(int position);

        void updateUi();

        void notifyInsert();

        void notifyRemove(int position);
    }
}
