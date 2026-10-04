package com.wadexhong.boxscore.objects

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

/**
 * Created by wade8 on 2018/5/2.
 */
data class TeamInfo @JvmOverloads constructor(
    val teamName: String,
    val teamId: String,
    val teamDetails: List<TeamDetail>? = null,
    val players: ArrayList<Player?>? = null
) : ExpandableGroup<TeamDetail>(teamName, teamDetails) {

    companion object {
        const val TEAM_NAME: Int = 0
        const val TEAM_ID: Int = 1
        const val TEAM_PLAYERS_AMOUT: Int = 2
        const val TEAM_HISTORY_AMOUNT: Int = 3
    }
}
