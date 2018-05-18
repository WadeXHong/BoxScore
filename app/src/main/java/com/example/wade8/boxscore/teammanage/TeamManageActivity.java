package com.example.wade8.boxscore.teammanage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.wade8.boxscore.R;

public class TeamManageActivity extends AppCompatActivity implements TeamManageContract.View{
    private static final String TAG = TeamManageActivity.class.getSimpleName();

    private TeamManageContract.Presenter mPresenter;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manage);
        mPresenter = new TeamManagePresenter(this, getSupportFragmentManager());

        mToolbar = findViewById(R.id.activity_teammanage_toolbar);
        setTeamMainToolbar();

        mPresenter.start();

    }

    @Override
    public void setPresenter(TeamManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setCreateTeamToolbar() {
        mToolbar.setTitle(R.string.createTeam);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_30dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setTeamMainToolbar() {
        mToolbar.setTitle(R.string.teamManage);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_30dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
