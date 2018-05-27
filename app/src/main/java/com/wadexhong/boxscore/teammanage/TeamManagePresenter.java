package com.wadexhong.boxscore.teammanage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.createplayer.CreatePlayerFragment;
import com.wadexhong.boxscore.createplayer.CreatePlayerPresenter;
import com.wadexhong.boxscore.createteam.CreateTeamDialogFragment;
import com.wadexhong.boxscore.createteam.CreateTeamPresenter;
import com.wadexhong.boxscore.teammain.TeamMainFragment;
import com.wadexhong.boxscore.teammain.TeamMainPresenter;
import com.wadexhong.boxscore.teamplayers.TeamPlayersFragment;
import com.wadexhong.boxscore.teamplayers.TeamPlayersPresenter;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamManagePresenter implements TeamManageContract.Presenter {

    private static final String TAG = TeamManagePresenter.class.getSimpleName();

    public static final String TEAM_MAIN = "TEAM_MAIN";
    public static final String CREATE_TEAM = "CREATE_TEAM";
    public static final String TEAM_PLAYERS = "TEAM_PLAYERS";
    public static final String CREATE_PLAYER = "CREATE_PLAYER";

    private final TeamManageContract.View mTeamManageView;

    private FragmentManager mFragmentManager;
    private TeamMainFragment mTeamMainFragment;
    private TeamMainPresenter mTeamMainPresenter;
    private TeamPlayersFragment mTeamPlayersFragment;
    private TeamPlayersPresenter mTeamPlayersPresenter;
    private CreateTeamDialogFragment mCreateTeamDialogFragment;
    private CreateTeamPresenter mCreateTeamPresenter;
    private CreatePlayerFragment mCreatePlayerFragment;
    private CreatePlayerPresenter mCreatePlayerPresenter;

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

        if (mTeamPlayersPresenter == null) mTeamPlayersPresenter = new TeamPlayersPresenter(mTeamPlayersFragment, this);

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
    public void transToCreatePlayer(String teamId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);

        if (mCreatePlayerFragment == null) mCreatePlayerFragment = CreatePlayerFragment.newInstance();

        if (mTeamPlayersFragment != null){
            transaction.hide(mTeamPlayersFragment);
            transaction.addToBackStack(TEAM_PLAYERS);
        }

        mCreatePlayerFragment.setTeamId(teamId);

        if (!mCreatePlayerFragment.isAdded()){
            transaction.add(R.id.activity_teammanage_framelayout, mCreatePlayerFragment, CREATE_PLAYER);
        }else {
            transaction.show(mCreatePlayerFragment);
        }
        transaction.commit();

        if (mCreatePlayerPresenter == null){
            mCreatePlayerPresenter = new CreatePlayerPresenter(mCreatePlayerFragment, this);
        }

        setCreatePlayerToolbar();
    }

    @Override
    public void onBackPressed() {
        mTeamManageView.onBackPressed();
    }

    @Override
    public void refreshMainUi() {
        mTeamMainPresenter.refreshUi();
    }


    private void setCreatePlayerToolbar() {
        mTeamManageView.setCreatePlayerToolbar();
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
