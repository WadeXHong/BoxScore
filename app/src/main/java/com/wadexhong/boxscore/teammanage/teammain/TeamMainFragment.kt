package com.wadexhong.boxscore.teammanage.teammain

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
import com.wadexhong.boxscore.teammanage.LegacyTeamManageActivity

class TeamMainFragment : Fragment() {

    private lateinit var viewModel: TeamMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TeamMainViewModelFactory(BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).teamDao())
        viewModel = ViewModelProvider(this, factory)[TeamMainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                TeamMainScreen(
                    uiState = uiState,
                    onCreateTeamClick = {
                        // Forward to LegacyTeamManageActivity which implements TeamManageContract.View
                        (activity as? LegacyTeamManageActivity)?.mPresenter?.transToCreateTeam()
                    },
                    onTeamPlayersClick = { teamId ->
                        (activity as? LegacyTeamManageActivity)?.mPresenter?.transToTeamPlayers(teamId)
                    },
                    onDeleteTeamClick = { teamId ->
                        viewModel.deleteTeam(teamId)
                    }
                )
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            // Update Toolbar when fragment becomes visible
            (activity as? LegacyTeamManageActivity)?.mPresenter?.setTeamMainToolbar()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): TeamMainFragment {
            return TeamMainFragment()
        }
    }
}
