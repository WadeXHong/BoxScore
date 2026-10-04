package com.wadexhong.boxscore.teammanage.createteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.database.BoxScoreDatabase

class CreateTeamDialogFragment : DialogFragment() {

    private lateinit var viewModel: CreateTeamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Remove the default dialog title and background to let Compose handle the dim and styling.
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar)

        val teamDao = BoxScoreDatabase.getDatabase(BoxScore.getAppContext()).teamDao()
        val factory = CreateTeamViewModelFactory(teamDao)
        viewModel = ViewModelProvider(this, factory)[CreateTeamViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                CreateTeamScreen(
                    uiState = uiState,
                    onConfirmClick = { teamName ->
                        viewModel.createTeam(teamName)
                    },
                    onCancelClick = {
                        dismissAllowingStateLoss()
                    },
                    onSuccess = {
                        dismissAllowingStateLoss()
                        // Note: We no longer need to call refreshMainUi() because TeamMain
                        // listens to the Flow<List<TeamInfoEntity>> from Room.
                    },
                    onErrorShown = {
                        viewModel.clearError()
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CreateTeamDialogFragment {
            return CreateTeamDialogFragment()
        }
    }
}
