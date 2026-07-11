package com.wadexhong.boxscore.teammanage.teammain

import com.wadexhong.boxscore.BasePresenter
import com.wadexhong.boxscore.BaseView

/**
 * Created by wade8 on 2018/5/18.
 */
interface TeamMainContract {
    interface View : BaseView<Presenter> {
        fun showTeams(teams: List<com.wadexhong.boxscore.objects.TeamInfo>)
    }

    interface Presenter : BasePresenter {
        fun queryTeamDataFromDatabase() // call dbhelper

        fun pressedCreateTeam()

        fun refreshToolBar()

        fun refreshUi()

        fun deleteTeam(teamId: String?)

        fun pressedTeamPlayers(teamId: String?)
    }
}
