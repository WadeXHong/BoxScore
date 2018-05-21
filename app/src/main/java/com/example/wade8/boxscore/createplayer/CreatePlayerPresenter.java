package com.example.wade8.boxscore.createplayer;

import com.example.wade8.boxscore.BoxScore;
import com.example.wade8.boxscore.objects.Player;
import com.example.wade8.boxscore.teammanage.TeamManageContract;

/**
 * Created by wade8 on 2018/5/21.
 */

public class CreatePlayerPresenter implements CreatePlayerContract.Presenter{

    private static final String TAG = CreatePlayerPresenter.class.getSimpleName();

    private final CreatePlayerContract.View mCreatePlayerView;
    private TeamManageContract.Presenter mTeamManagePresenter;

    public CreatePlayerPresenter(CreatePlayerContract.View createPlayerView, TeamManageContract.Presenter teamManagePresenter) {
        mCreatePlayerView = createPlayerView;
        mTeamManagePresenter = teamManagePresenter;

        mCreatePlayerView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void refreshToolBar() {
        mTeamManagePresenter.setTeamPlayersToolbar();
    }

    @Override
    public void createPlayer(String teamId, Player player) {
        BoxScore.getTeamDbHelper().createPlayerInDb(teamId, player);
        mTeamManagePresenter.onBackPressed();
    }

    @Override
    public boolean checkNumberIsExist(String teamId, String playerNumber) {
        return BoxScore.getTeamDbHelper().checkNumberIsExistInDb(teamId, playerNumber);
    }
}
