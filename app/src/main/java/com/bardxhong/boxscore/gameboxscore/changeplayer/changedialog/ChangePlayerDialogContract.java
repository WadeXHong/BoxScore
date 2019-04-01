package com.bardxhong.boxscore.gameboxscore.changeplayer.changedialog;

import com.bardxhong.boxscore.BasePresenter;
import com.bardxhong.boxscore.BaseView;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface ChangePlayerDialogContract {

    interface View extends BaseView<Presenter>{

        void setRecyclerView(ChangePlayerDialogAdapter mAdapter);

        void dismiss();
    }

    interface Presenter extends BasePresenter{

        void initAdapter();

        void exchangePlayer(int layoutPosition);
    }
}
