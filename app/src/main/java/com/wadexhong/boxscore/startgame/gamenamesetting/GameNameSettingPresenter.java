package com.wadexhong.boxscore.startgame.gamenamesetting;

import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.TeamInfo;
import com.wadexhong.boxscore.startgame.StartGameContract;
import com.wadexhong.boxscore.BoxScore;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/1.
 */

public class GameNameSettingPresenter implements GameNameSettingContract.Presenter {

    private final String TAG = GameNameSettingPresenter.class.getSimpleName();

    private final GameNameSettingContract.View mGameNameSettingView;
    private StartGameContract.Presenter mStartGamePresenter;

    private ArrayList<TeamInfo> mTeamInfos;

    public GameNameSettingPresenter(GameNameSettingContract.View gameNameSettingView, StartGameContract.Presenter startGamePresenter) {
        mGameNameSettingView = gameNameSettingView;
        mStartGamePresenter = startGamePresenter;

        mGameNameSettingView.setPresenter(this);
    }

    public void getDataFromView(GameInfo gameInfo) {
        mGameNameSettingView.getSettingResult(gameInfo);
    }

    public boolean checkInputIsLegal() {
        String[] input = mGameNameSettingView.getCheckedInput();
        return !input[0].trim().equals("");
    }

    @Override
    public ArrayList<TeamInfo> getTeamInfos() {
        return mTeamInfos;
    }

    @Override
    public void setDefaultPlayerList(String teamId) {
        mStartGamePresenter.setDefaultPlayerList(teamId);
    }

    @Override
    public void start() {
        mTeamInfos = BoxScore.getTeamDbHelper().getTeamsForAdapter(6);
        if (mTeamInfos.size() == 0){
            mStartGamePresenter.noLegalTeam();
        }
    }
}
