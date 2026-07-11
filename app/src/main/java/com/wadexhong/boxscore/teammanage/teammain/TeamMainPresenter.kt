package com.wadexhong.boxscore.teammanage.teammain

import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.teammanage.TeamManageContract

import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.objects.TeamDetail
import com.wadexhong.boxscore.objects.TeamInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Created by wade8 on 2018/5/18.
 */
@Deprecated("Replaced by TeamMainViewModel and TeamMainScreen Compose UI in Phase 6/7")
class TeamMainPresenter(
    private val mTeamMainView: TeamMainContract.View,
    private val mTeamManagePresenter: TeamManageContract.Presenter
) : TeamMainContract.Presenter, CoroutineScope by MainScope() {
    init {
        mTeamMainView.setPresenter(this)
        queryTeamDataFromDatabase()
    }

    override fun start() {
    }

    override fun queryTeamDataFromDatabase() {
        launch {
            BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).teamDao().getAllTeams().collect { entities ->
                val teamInfos = entities.map { entity ->
                    val detail = TeamDetail(
                        teamName = entity.teamName,
                        teamId = entity.teamId,
                        teamPlayersAmount = entity.playersAmount,
                        teamHistoryAmount = entity.historyAmount
                    )
                    TeamInfo(entity.teamName, listOf(detail))
                }
                mTeamMainView.showTeams(teamInfos)
            }
        }
    }

    override fun refreshToolBar() {
        mTeamManagePresenter.setTeamMainToolbar()
    }

    override fun refreshUi() {
        // Handled reactively by Room Flow
    }

    override fun pressedCreateTeam() {
        mTeamManagePresenter.transToCreateTeam()
    }

    override fun pressedTeamPlayers(teamId: String?) {
        mTeamManagePresenter.transToTeamPlayers(teamId)
    }

    override fun deleteTeam(teamId: String?) {
        if (teamId != null) {
            launch {
                BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).teamDao().deleteTeamById(teamId)
            }
        }
    }

    companion object {
        private val TAG: String = TeamMainPresenter::class.java.getSimpleName()
    }
}
