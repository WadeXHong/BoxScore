package com.example.wade8.boxscore.dialogfragment;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface PlayerSelecterContract {
    interface View extends BaseView<Presenter>{

        void dismiss();
    }

    interface Presenter extends BasePresenter{

        void EditDataInDB(Player player, int type);
    }
}
