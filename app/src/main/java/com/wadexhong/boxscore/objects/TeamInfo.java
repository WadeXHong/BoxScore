package com.wadexhong.boxscore.objects;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/2.
 */

public class TeamInfo extends ExpandableGroup<TeamDetail>{

    public static final int TEAM_NAME = 0;
    public static final int TEAM_ID = 1;
    public static final int TEAM_PLAYERS_AMOUT = 2;
    public static final int TEAM_HISTORY_AMOUNT = 3;

    private String mTeamName;
    private String mTeamId;
    private ArrayList<Player> mPlayers;

    public TeamInfo(String teamName, String teamId, List<TeamDetail> items){
        this(teamName, items);
        mTeamId = teamId;
    }

    public TeamInfo(String teamName, List<TeamDetail> items) {
        super(teamName, items);
        mTeamName = teamName;
    }


    public ArrayList<Player> getmPlayers() {
        return mPlayers;
    }

    public void setmPlayers(ArrayList<Player> mPlayers) {
        this.mPlayers = mPlayers;
    }


    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }
}
