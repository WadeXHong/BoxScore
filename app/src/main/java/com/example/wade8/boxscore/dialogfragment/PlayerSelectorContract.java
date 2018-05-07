package com.example.wade8.boxscore.dialogfragment;

import com.example.wade8.boxscore.BasePresenter;
import com.example.wade8.boxscore.BaseView;
import com.example.wade8.boxscore.objects.GameInfo;
import com.example.wade8.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/4.
 */

public interface PlayerSelectorContract {
    interface View extends BaseView<Presenter>{

        void dismiss();
    }

    interface Presenter extends BasePresenter{

        void editDataInDB(int position, int type);

        ArrayList<Player> getPlayerOnCourt();
    }
}
