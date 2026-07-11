package com.wadexhong.boxscore.teammanage.teamplayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.teammanage.TeamManageActivity

class TeamPlayersFragment : Fragment() {

    private var mTeamId: String? = null
    private lateinit var viewModel: TeamPlayersViewModel

    fun setTeamId(teamId: String?) {
        mTeamId = teamId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val teamId = mTeamId ?: return
        val playerDao = BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).playerDao()
        val factory = TeamPlayersViewModelFactory(teamId, playerDao)
        viewModel = ViewModelProvider(this, factory)[TeamPlayersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                TeamPlayersScreen(
                    uiState = uiState,
                    onCreatePlayerClick = {
                        (activity as? TeamManageActivity)?.mPresenter?.transToCreatePlayer(mTeamId)
                    },
                    onDeletePlayerClick = { playerId ->
                        viewModel.deletePlayer(playerId)
                    }
                )
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (activity as? TeamManageActivity)?.mPresenter?.setTeamPlayersToolbar()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): TeamPlayersFragment {
            return TeamPlayersFragment()
        }
    }
}
