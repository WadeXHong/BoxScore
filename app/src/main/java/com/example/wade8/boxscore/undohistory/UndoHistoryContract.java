package com.example.wade8.boxscore.undohistory;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.UndoHistoryAdapter;

/**
 * Created by wade8 on 2018/5/10.
 */

public interface UndoHistoryContract {
    interface View extends BaseView<Presenter>{

        void setAdapter(UndoHistoryAdapter mAdapter);
    }

    interface Presenter extends BasePresenter{

        void createAdapter();
    }
}
