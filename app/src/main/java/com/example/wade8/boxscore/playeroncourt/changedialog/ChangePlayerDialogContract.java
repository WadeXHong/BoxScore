package com.example.wade8.boxscore.playeroncourt.changedialog;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.adapter.ChangePlayerDialogAdapter;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface ChangePlayerDialogContract {
    interface View extends BaseView<Presenter>{

        void dismiss();

        void setRecyclerView(ChangePlayerDialogAdapter mAdapter);
    }

    interface Presenter extends BasePresenter{

        void initAdapter();

        void exchangePlayer(int layoutPosition);
    }
}
