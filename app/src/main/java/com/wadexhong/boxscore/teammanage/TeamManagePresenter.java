package com.wadexhong.boxscore.teammanage;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.teammanage.createteam.CreateTeamDialogFragment;
import com.wadexhong.boxscore.teammanage.teammain.TeamMainFragment;
import com.wadexhong.boxscore.teammanage.teamplayers.TeamPlayersFragment;
import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.CreatePlayerFragment;

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
    private TeamPlayersFragment mTeamPlayersFragment;
    private CreateTeamDialogFragment mCreateTeamDialogFragment;
    private CreatePlayerFragment mCreatePlayerFragment;

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

        setCreatePlayerToolbar();
    }

    @Override
    public void onBackPressed() {
        mTeamManageView.onBackPressed();
    }

    @Override
    public void refreshMainUi() {
        // UI now auto-updates via Room Flow and Jetpack Compose State
    }


    @Override
    public void setCreatePlayerToolbar() {
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
