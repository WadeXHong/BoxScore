package com.wadexhong.boxscore.startgame

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberStepper(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.subtitle1)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onValueChange(value - 1) }) {
                Text("-")
            }
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.h6
            )
            Button(onClick = { onValueChange(value + 1) }) {
                Text("+")
            }
        }
    }
}

@Composable
fun DetailSettingPage(viewModel: StartGameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        NumberStepper(
            label = "Quarter Length (min)",
            value = uiState.quarterLength,
            onValueChange = { viewModel.onQuarterLengthChanged(it) }
        )
        NumberStepper(
            label = "Total Quarters",
            value = uiState.totalQuarters,
            onValueChange = { viewModel.onTotalQuartersChanged(it) }
        )
        NumberStepper(
            label = "Max Fouls",
            value = uiState.maxFouls,
            onValueChange = { viewModel.onMaxFoulsChanged(it) }
        )
        NumberStepper(
            label = "Timeouts (1st Half)",
            value = uiState.timeoutsFirstHalf,
            onValueChange = { viewModel.onTimeoutsFirstHalfChanged(it) }
        )
        NumberStepper(
            label = "Timeouts (2nd Half)",
            value = uiState.timeoutsSecondHalf,
            onValueChange = { viewModel.onTimeoutsSecondHalfChanged(it) }
        )
    }
}
