package com.wadexhong.boxscore.startgame

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun GameNameSettingPage(viewModel: StartGameViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var teamDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        
        Text("Your Team")
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = uiState.selectedTeam?.teamName ?: "Select a team",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().clickable { teamDropdownExpanded = true },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, "dropdown") }
            )
            DropdownMenu(
                expanded = teamDropdownExpanded,
                onDismissRequest = { teamDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.teams.forEach { team ->
                    DropdownMenuItem(onClick = {
                        viewModel.onTeamSelected(team)
                        teamDropdownExpanded = false
                    }) {
                        Text(team.teamName ?: "")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Game Title")
        OutlinedTextField(
            value = uiState.gameTitle,
            onValueChange = { viewModel.onGameTitleChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Opponent Name")
        OutlinedTextField(
            value = uiState.opponentName,
            onValueChange = { viewModel.onOpponentNameChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (uiState.isStep1Error) Modifier.border(2.dp, Color.Red) else Modifier
                ),
            singleLine = true,
            isError = uiState.isStep1Error
        )
        if (uiState.isStep1Error) {
            Text("Opponent name cannot be empty", color = Color.Red, style = MaterialTheme.typography.caption)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Date")
        OutlinedTextField(
            value = uiState.gameDate.ifEmpty { "Select Date" },
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        viewModel.onGameDateChanged("$year-${month + 1}-$dayOfMonth")
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        )
    }
}
