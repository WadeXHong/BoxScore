package com.example.wade8.boxscore.historymain;

import android.database.Cursor;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/22.
 */

public interface HistoryMainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        Cursor getGameHistory();
    }
}
