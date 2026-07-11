package com.wadexhong.boxscore.teammanage.teamplayers.createplayer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R

@Composable
fun CreatePlayerScreen(
    uiState: CreatePlayerUiState,
    onConfirmClick: (name: String, number: String, isC: Boolean, isPF: Boolean, isSF: Boolean, isSG: Boolean, isPG: Boolean) -> Unit,
    onSuccess: () -> Unit,
    onErrorShown: () -> Unit
) {
    val context = LocalContext.current

    var playerName by remember { mutableStateOf("") }
    var playerNumber by remember { mutableStateOf("") }

    // Positions state
    var isC by remember { mutableStateOf(false) }
    var isPF by remember { mutableStateOf(false) }
    var isSF by remember { mutableStateOf(false) }
    var isSG by remember { mutableStateOf(false) }
    var isPG by remember { mutableStateOf(false) }

    // Handle side effects
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            onErrorShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDarkBackground))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = playerName,
            onValueChange = { if (it.length <= 20) playerName = it },
            label = { Text(stringResource(id = R.string.playerName), color = Color.Gray) },
            placeholder = { Text(stringResource(id = R.string.playerNameHint), color = Color.DarkGray) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = colorResource(id = R.color.colorButtonInMain),
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = playerNumber,
            onValueChange = { if (it.length <= 2) playerNumber = it },
            label = { Text(stringResource(id = R.string.playerNumber), color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = colorResource(id = R.color.colorButtonInMain),
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Positions Checkboxes
        Text("Positions (Optional)", color = Color.White, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PositionCheckbox("C", isC) { isC = it }
            PositionCheckbox("PF", isPF) { isPF = it }
            PositionCheckbox("SF", isSF) { isSF = it }
            PositionCheckbox("SG", isSG) { isSG = it }
            PositionCheckbox("PG", isPG) { isPG = it }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onConfirmClick(playerName, playerNumber, isC, isPF, isSF, isSG, isPG)
            },
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorButtonInMain)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(id = R.string.confirm), color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun PositionCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.colorButtonInMain),
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}
