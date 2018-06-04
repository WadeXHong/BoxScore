package com.wadexhong.boxscore.gameboxscore.changeplayer.changedialog;

import com.wadexhong.boxscore.BasePresenter;
import com.wadexhong.boxscore.BaseView;

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
