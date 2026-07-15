package com.wadexhong.boxscore.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    isShowLogout: Boolean,
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text("Settings", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))
        }

        item {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Display", style = MaterialTheme.typography.h6)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Manual Brightness")
                        Switch(
                            checked = uiState.brightness >= 0f,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    viewModel.updateBrightness(0.5f)
                                } else {
                                    viewModel.updateBrightness(-1f)
                                }
                            }
                        )
                    }
                    if (uiState.brightness >= 0f) {
                        Slider(
                            value = uiState.brightness,
                            onValueChange = { viewModel.updateBrightness(it) },
                            valueRange = 0f..1f
                        )
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Multi-Finger Gestures", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))

                    val gestureTitles = context.resources.getStringArray(R.array.gesture_setting)
                    val keys = listOf(
                        kotlin.Pair(SettingsRepository.DOUBLE_UP, uiState.doubleUp),
                        kotlin.Pair(SettingsRepository.DOUBLE_DOWN, uiState.doubleDown),
                        kotlin.Pair(SettingsRepository.DOUBLE_LEFT, uiState.doubleLeft),
                        kotlin.Pair(SettingsRepository.DOUBLE_RIGHT, uiState.doubleRight),
                        kotlin.Pair(SettingsRepository.TRIPLE_UP, uiState.tripleUp),
                        kotlin.Pair(SettingsRepository.TRIPLE_DOWN, uiState.tripleDown),
                        kotlin.Pair(SettingsRepository.TRIPLE_LEFT, uiState.tripleLeft),
                        kotlin.Pair(SettingsRepository.TRIPLE_RIGHT, uiState.tripleRight)
                    )

                    keys.forEachIndexed { index, (key, value) ->
                        GestureRow(
                            title = if (index < gestureTitles.size) gestureTitles[index] else "",
                            currentValue = value,
                            onValueChange = { newValue ->
                                viewModel.updateGesture(key, newValue)
                            }
                        )
                    }
                }
            }
        }

        if (isShowLogout) {
            item {
                Button(
                    onClick = { viewModel.signOut(onFinish) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Log Out", color = MaterialTheme.colors.onError)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GestureRow(
    title: String,
    currentValue: Int,
    onValueChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val choices = listOf(
        -1 to R.string.none,
        Constants.TYPE_CHOICE_INT[1] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[1]),
        Constants.TYPE_CHOICE_INT[2] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[2]),
        Constants.TYPE_CHOICE_INT[3] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[3]),
        Constants.TYPE_CHOICE_INT[4] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[4]),
        Constants.TYPE_CHOICE_INT[5] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[5]),
        Constants.TYPE_CHOICE_INT[6] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[6]),
        Constants.TYPE_CHOICE_INT[7] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[7]),
        Constants.TYPE_CHOICE_INT[8] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[8]),
        Constants.TYPE_CHOICE_INT[9] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[9]),
        Constants.TYPE_CHOICE_INT[10] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[10]),
        Constants.TYPE_CHOICE_INT[11] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[11]),
        Constants.TYPE_CHOICE_INT[12] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[12]),
        Constants.TYPE_CHOICE_INT[13] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[13]),
        Constants.TYPE_CHOICE_INT[14] to Constants.TITLE_SPARSE_ARRAY.get(Constants.TYPE_CHOICE_INT[14])
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, modifier = Modifier.weight(1f))
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            val stringResId = if (currentValue == -1) R.string.none else Constants.TITLE_SPARSE_ARRAY.get(currentValue)
            val currentLabel = if (stringResId != 0) stringResource(stringResId) else stringResource(R.string.none)
            
            OutlinedTextField(
                value = currentLabel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                choices.forEach { (actionId, resId) ->
                    DropdownMenuItem(onClick = {
                        onValueChange(actionId)
                        expanded = false
                    }) {
                        Text(if (resId != 0) stringResource(resId) else stringResource(R.string.none))
                    }
                }
            }
        }
    }
}
