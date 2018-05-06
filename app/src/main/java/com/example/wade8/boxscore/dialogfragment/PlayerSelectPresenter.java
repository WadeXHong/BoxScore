package com.example.wade8.boxscore.dialogfragment;

import android.util.Log;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.dbhelper.GameDataDbHelper;
import com.example.wade8.boxscore.objects.Player;

/**
 * Created by wade8 on 2018/5/4.
 */

public class PlayerSelectPresenter implements PlayerSelecterContract.Presenter {

    private static final String TAG = PlayerSelectPresenter.class.getSimpleName();

    private final PlayerSelecterContract.View mPlayerSelectView;

    public PlayerSelectPresenter(PlayerSelecterContract.View mPlayerSelectView) {
        this.mPlayerSelectView = mPlayerSelectView;
        mPlayerSelectView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void EditDataInDB(Player player, int type) {
        //TODO write data into DB
        GameDataDbHelper mGameDataDbHelper = BoxScore.getGameDataDbHelper();
        mGameDataDbHelper.writeGameData(player,type);
        //TODO add to UNDOList
        String name = player.getmName();
        String number = player.getmNumber();
        Log.d(TAG,"number " + number +" " + name + " " + type +" + 1");

        mPlayerSelectView.dismiss();
    }
}
