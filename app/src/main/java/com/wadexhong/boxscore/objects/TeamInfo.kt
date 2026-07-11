package com.wadexhong.boxscore.objects

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

/**
 * Created by wade8 on 2018/5/2.
 */
class TeamInfo(var teamName: String?, items: List<TeamDetail>?) :
    ExpandableGroup<TeamDetail>(
        teamName, items
    ) {
    var teamId: String? = null
    private var mPlayers: ArrayList<Player?>? = null

    constructor(teamName: String?, teamId: String?, items: List<TeamDetail>?) : this(
        teamName,
        items
    ) {
        this.teamId = teamId
    }


    fun getmPlayers(): ArrayList<Player?>? {
        return mPlayers
    }

    fun setmPlayers(players: ArrayList<Player?>?) {
        mPlayers = players
    }


    companion object {
        const val TEAM_NAME: Int = 0
        const val TEAM_ID: Int = 1
        const val TEAM_PLAYERS_AMOUT: Int = 2
        const val TEAM_HISTORY_AMOUNT: Int = 3
    }
}
