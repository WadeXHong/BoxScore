package com.wadexhong.boxscore.teammanage.teamplayers;

import android.database.Cursor;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.teammanage.TeamManageContract;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamPlayersPresenter implements TeamPlayersContract.Presenter{

    private static final String TAG = TeamPlayersPresenter.class.getSimpleName();

    private final TeamPlayersContract.View mCreatePlayerView;
    private TeamManageContract.Presenter mTeamManagePresenter;

    public TeamPlayersPresenter(TeamPlayersContract.View createPlayerView, TeamManageContract.Presenter teamManagePresenter) {
        mCreatePlayerView = createPlayerView;
        mTeamManagePresenter = teamManagePresenter;

        mCreatePlayerView.setPresenter(this);
    }


    @Override
    public Cursor getPlayers(String teamId) {
        return BoxScore.getTeamDbHelper().getPlayersFromDb(teamId);
    }

    @Override
    public void pressedCreatePlayer(String teamId) {
        mTeamManagePresenter.transToCreatePlayer(teamId);
    }

    @Override
    public void deletePlayer(String teamId, String playerId) {
        BoxScore.getTeamDbHelper().deletePlayerInDb(teamId, playerId);
    }

    @Override
    public void transToPlayerDetail(String playerId) {

    }

    @Override
    public void refreshUi() {
        mCreatePlayerView.refreshUi();
    }

    @Override
    public void start() {

    }
}
