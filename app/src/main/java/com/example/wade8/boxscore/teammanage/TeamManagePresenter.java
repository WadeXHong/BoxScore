package com.example.wade8.boxscore.teammanage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.createteam.CreateTeamDialogFragment;
import com.example.wade8.boxscore.createteam.CreateTeamPresenter;
import com.example.wade8.boxscore.teamplayers.TeamPlayersFragment;
import com.example.wade8.boxscore.teamplayers.TeamPlayersPresenter;
import com.example.wade8.boxscore.teammain.TeamMainFragment;
import com.example.wade8.boxscore.teammain.TeamMainPresenter;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamManagePresenter implements TeamManageContract.Presenter {

    private static final String TAG = TeamManagePresenter.class.getSimpleName();

    public static final String TEAM_MAIN = "TEAM_MAIN";
    public static final String CREATE_TEAM = "CREATE_TEAM";
    public static final String TEAM_PLAYERS = "TEAM_PLAYERS";

    private final TeamManageContract.View mTeamManageView;

    private FragmentManager mFragmentManager;
    private TeamMainFragment mTeamMainFragment;
    private TeamMainPresenter mTeamMainPresenter;
    private TeamPlayersFragment mTeamPlayersFragment;
    private TeamPlayersPresenter mTeamPlayersPresenter;
    private CreateTeamDialogFragment mCreateTeamDialogFragment;
    private CreateTeamPresenter mCreateTeamPresenter;

    public TeamManagePresenter(TeamManageContract.View teamManageView, FragmentManager supportFragmentManager) {
        mTeamManageView = teamManageView;
        mFragmentManager = supportFragmentManager;

        mTeamManageView.setPresenter(this);
    }

    @Override
    public void start() {
        transToTeamMain();
    }

    @Override
    public void transToCreateTeam() {
        if (mCreateTeamDialogFragment == null) mCreateTeamDialogFragment = CreateTeamDialogFragment.newInstance();
        if (mCreateTeamPresenter == null) mCreateTeamPresenter = new CreateTeamPresenter(mCreateTeamDialogFragment, this);
        mCreateTeamDialogFragment.show(mFragmentManager,CREATE_TEAM);
    }

    @Override
    public void transToTeamPlayers(String teamId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);


        if (mTeamPlayersFragment == null) mTeamPlayersFragment = TeamPlayersFragment.newInstance();
        if (mTeamMainFragment != null) {
            transaction.hide(mTeamMainFragment);
            transaction.addToBackStack(TEAM_MAIN);
        }

        mTeamPlayersFragment.setTeamId(teamId);

        if (!mTeamPlayersFragment.isAdded()){
            transaction.add(R.id.activity_teammanage_framelayout, mTeamPlayersFragment, TEAM_PLAYERS);
        }else {
            transaction.show(mTeamPlayersFragment);
        }

        transaction.commit();

        if (mTeamPlayersPresenter == null) mTeamPlayersPresenter = new TeamPlayersPresenter(mTeamPlayersFragment);

        setTeamPlayersToolbar();
    }

    @Override
    public void transToTeamMain() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mTeamMainFragment == null) mTeamMainFragment = TeamMainFragment.newInstance();
        if (mTeamPlayersFragment != null) transaction.remove(mTeamPlayersFragment);
        if (!mTeamMainFragment.isAdded()){
            transaction.add(R.id.activity_teammanage_framelayout, mTeamMainFragment, TEAM_MAIN);
        }else {
            transaction.show(mTeamMainFragment);
        }
        transaction.commit();

        if (mTeamMainPresenter == null){
            mTeamMainPresenter = new TeamMainPresenter(mTeamMainFragment, this);
        }

        setTeamMainToolbar();
    }

    @Override
    public void refreshMainUi() {
        mTeamMainPresenter.refreshUi();
    }

    @Override
    public void setTeamMainToolbar() {
        mTeamManageView.setTeamMainToolbar();
    }

    @Override
    public void setTeamPlayersToolbar() {
        mTeamManageView.setTeamPlayersToolbar();
    }
}
