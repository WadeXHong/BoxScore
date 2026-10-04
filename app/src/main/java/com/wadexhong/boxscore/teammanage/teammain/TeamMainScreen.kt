package com.wadexhong.boxscore.teammanage.teammain

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.objects.TeamDetail
import com.wadexhong.boxscore.objects.TeamInfo
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TeamMainScreen(
    uiState: TeamMainUiState,
    onCreateTeamClick: () -> Unit,
    onTeamPlayersClick: (String) -> Unit,
    onDeleteTeamClick: (String) -> Unit
) {
    var teamToDelete by remember { mutableStateOf<TeamInfo?>(null) }
    var expandedTeamId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDarkBackground))
    ) {
        // Create Team Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable { onCreateTeamClick() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Team",
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.createTeam),
                fontSize = 20.sp,
                color = colorResource(id = R.color.colorButtonInMain)
            )
        }

        // Teams List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.teams) { team ->
                val isExpanded = expandedTeamId == team.teamId
                TeamListItem(
                    team = team,
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        expandedTeamId = if (isExpanded) null else team.teamId
                    },
                    onDeleteClick = { teamToDelete = team }
                )
                
                // Expandable Detail
                AnimatedVisibility(visible = isExpanded) {
                    team.items?.firstOrNull()?.let { detail ->
                        TeamDetailItem(
                            detail = detail,
                            onTeamPlayersClick = { onTeamPlayersClick(team.teamId!!) }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    teamToDelete?.let { team ->
        AlertDialog(
            onDismissRequest = { teamToDelete = null },
            title = { Text(stringResource(id = R.string.dialog_delete_team_title)) },
            text = { Text(stringResource(id = R.string.dialog_delete_team_message, team.teamName ?: "")) },
            confirmButton = {
                TextButton(onClick = {
                    team.teamId?.let(onDeleteTeamClick)
                    teamToDelete = null
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { teamToDelete = null }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun TeamListItem(
    team: TeamInfo,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Legacy rotated the arrow 0 -> 90 degrees over 150ms on expand, and back on collapse.
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "arrowRotation"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable { onToggleExpand() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_notsowhite_30dp),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .rotate(arrowRotation)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = team.teamName ?: "",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_forever_white_24dp),
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier
                .size(35.dp)
                .clickable { onDeleteClick() }
        )
    }
}

@Composable
fun TeamDetailItem(
    detail: TeamDetail,
    onTeamPlayersClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF222222)) // Darker background for detail
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onTeamPlayersClick() }
        ) {
            Text(text = stringResource(id = R.string.playerlist), color = Color.Gray)
            Text(text = stringResource(id = R.string.playersAmount, detail.teamPlayersAmount), color = Color.White, fontSize = 24.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.gameHistory), color = Color.Gray)
            Text(text = stringResource(id = R.string.historyAmount, detail.teamHistoryAmount), color = Color.White, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamMainScreenPreview() {
    val sampleTeams = listOf(
        TeamInfo(
            teamName = "Lakers",
            teamId = "1",
            teamDetails = listOf(TeamDetail(teamName = "Lakers", teamId = "1", teamPlayersAmount = 15, teamHistoryAmount = 82))
        ),
        TeamInfo(
            teamName = "Warriors",
            teamId = "2",
            teamDetails = listOf(TeamDetail(teamName = "Warriors", teamId = "2", teamPlayersAmount = 14, teamHistoryAmount = 78))
        )
    )
    val uiState = TeamMainUiState(
        teams = sampleTeams,
        isLoading = false
    )
    TeamMainScreen(
        uiState = uiState,
        onCreateTeamClick = {},
        onTeamPlayersClick = {},
        onDeleteTeamClick = {}
    )
}
