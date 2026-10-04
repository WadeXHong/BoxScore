package com.wadexhong.boxscore.teammanage.createteam

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R

@Composable
fun CreateTeamScreen(
    uiState: CreateTeamUiState,
    onConfirmClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSuccess: () -> Unit,
    onErrorShown: () -> Unit
) {
    val context = LocalContext.current
    var teamName by remember { mutableStateOf("") }

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

    // Full screen background for the dialog overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDialogBackground))
            .clickable(onClick = onCancelClick),
        contentAlignment = Alignment.Center
    ) {
        // White Card
        Column(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                // Consume taps so they don't reach the dismiss-on-tap background behind the card.
                .pointerInput(Unit) { detectTapGestures { } }
        ) {
            Text(
                text = stringResource(id = R.string.pleaseInputTeamName),
                color = colorResource(id = R.color.colorTextInMainButton),
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )

            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 3.dp)
            )

            OutlinedTextField(
                value = teamName,
                onValueChange = {
                    val input = it.trimStart(' ')
                    if (input.length <= 20) teamName = input
                },
                placeholder = { Text(stringResource(id = R.string.opponentNameHint)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = colorResource(id = R.color.colorButtonInMain),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = colorResource(id = R.color.colorDarkGray),
                        fontSize = 20.sp
                    )
                }

                TextButton(
                    onClick = { onConfirmClick(teamName) },
                    enabled = !uiState.isLoading && teamName.isNotBlank(),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm),
                        color = colorResource(id = R.color.colorDarkGray),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
