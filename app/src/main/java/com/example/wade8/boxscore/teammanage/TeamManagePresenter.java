package com.example.wade8.boxscore.teammanage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.wade8.boxscore.R;
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

    public TeamManagePresenter(TeamManageContract.View teamManageView, FragmentManager supportFragmentManager) {
        mTeamManageView = teamManageView;
        mFragmentManager = supportFragmentManager;

        mTeamManageView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void transToCreateTeam() {

    }

    @Override
    public void transToPlayers() {

    }

    @Override
    public void transToTeamMain() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mTeamMainFragment == null) mTeamMainFragment = TeamMainFragment.newInstance();
//        if (mCreateTeamFragement != null) transaction.remove(mCreateTeamFragment); //TODO  新增球對頁面
        if (!mTeamMainFragment.isAdded()){
            transaction.add(R.id.activity_teammanage_framelayout, mTeamMainFragment, TEAM_MAIN);
        }else {
            transaction.show(mTeamMainFragment);
        }
        transaction.commit();

        if (mTeamMainPresenter == null){
            mTeamMainPresenter = new TeamMainPresenter(mTeamMainFragment);
        }

        mTeamManageView.setTeamMainToolbar();
    }
}
