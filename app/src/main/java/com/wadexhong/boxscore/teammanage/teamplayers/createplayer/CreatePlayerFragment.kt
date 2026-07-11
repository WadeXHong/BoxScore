package com.wadexhong.boxscore.teammanage.teamplayers.createplayer

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

class CreatePlayerFragment : Fragment() {

    private var mTeamId: String? = null
    private lateinit var viewModel: CreatePlayerViewModel

    fun setTeamId(teamId: String?) {
        mTeamId = teamId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val teamId = mTeamId ?: return
        val playerDao = BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).playerDao()
        val factory = CreatePlayerViewModelFactory(teamId, playerDao)
        viewModel = ViewModelProvider(this, factory)[CreatePlayerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                CreatePlayerScreen(
                    uiState = uiState,
                    onConfirmClick = { name, number, isC, isPF, isSF, isSG, isPG ->
                        viewModel.createPlayer(name, number, isC, isPF, isSF, isSG, isPG)
                    },
                    onSuccess = {
                        // Pop backstack to return to TeamPlayersFragment
                        (activity as? TeamManageActivity)?.onBackPressed()
                    },
                    onErrorShown = {
                        viewModel.clearError()
                    }
                )
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (activity as? TeamManageActivity)?.mPresenter?.setCreatePlayerToolbar()
        }
    }

    companion object {
        const val POSITION_C = 0
        const val POSITION_PF = 1
        const val POSITION_SF = 2
        const val POSITION_SG = 3
        const val POSITION_PG = 4

        @JvmStatic
        fun newInstance(): CreatePlayerFragment {
            return CreatePlayerFragment()
        }
    }
}
