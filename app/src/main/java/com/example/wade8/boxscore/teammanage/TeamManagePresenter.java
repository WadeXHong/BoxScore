package com.example.wade8.boxscore.teammanage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.createteam.CreateTeamFragment;
import com.example.wade8.boxscore.createteam.CreateTeamPresenter;
import com.example.wade8.boxscore.teammain.TeamMainFragment;
import com.example.wade8.boxscore.teammain.TeamMainPresenter;

/**
 * Created by wade8 on 2018/5/18.
 */

public class TeamManagePresenter implements TeamManageContract.Presenter {

    private static final String TAG = TeamManagePresenter.class.getSimpleName();

    public static final String TEAM_MAIN = "TEAM_MAIN";
    public static final String CREATE_TEAM = "CREATE_TEAM";

    private final TeamManageContract.View mTeamManageView;

    private FragmentManager mFragmentManager;
    private TeamMainFragment mTeamMainFragment;
    private TeamMainPresenter mTeamMainPresenter;
    private CreateTeamFragment mCreateTeamFragment;
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
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left_fragment,R.anim.slide_left_fragment);


        if (mCreateTeamFragment == null) mCreateTeamFragment = CreateTeamFragment.newInstance();
        if (mTeamMainFragment != null) {
            transaction.hide(mTeamMainFragment);
            transaction.addToBackStack(TEAM_MAIN);
        }
        if (!mCreateTeamFragment.isAdded()){
            transaction.add(R.id.activity_teammanage_framelayout, mCreateTeamFragment, CREATE_TEAM);
        }else {
            transaction.show(mCreateTeamFragment);
        }
        transaction.commit();

        if (mCreateTeamPresenter == null){
            mCreateTeamPresenter = new CreateTeamPresenter(mCreateTeamFragment);
        }

        setCreateTeamToolbar();
    }

    @Override
    public void transToPlayers() {

    }

    @Override
    public void transToTeamMain() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left_fragment,R.anim.slide_left_fragment);

        if (mTeamMainFragment == null) mTeamMainFragment = TeamMainFragment.newInstance();
        if (mCreateTeamFragment != null) transaction.remove(mCreateTeamFragment);
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
    public void setTeamMainToolbar() {
        mTeamManageView.setTeamMainToolbar();
    }

    @Override
    public void setCreateTeamToolbar() {
        mTeamManageView.setCreateTeamToolbar();
    }
}
