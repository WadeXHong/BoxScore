package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R

@Composable
fun ScoreboardSection(
    state: GameUiState,
    onMakeupClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    onTableClicked: () -> Unit = {},
    onQuarterClicked: () -> Unit = {},
    onYourFoulClicked: () -> Unit = {},
    onOpponentScorePlus: () -> Unit = {},
    onOpponentScoreMinus: () -> Unit = {},
    onOpponentFoulPlus: () -> Unit = {},
    onOpponentFoulMinus: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(colorResource(id = R.color.colorDarkBackground))
    ) {
        // Top Bar Icons
        IconButton(
            onClick = onSaveClicked,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 23.dp, top = 16.dp)
                .width(30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = "Save",
                tint = colorResource(id = R.color.colorButtonInMain)
            )
        }

        IconButton(
            onClick = onSettingsClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 23.dp, top = 16.dp)
                .width(35.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting_white),
                contentDescription = "Settings",
                tint = colorResource(id = R.color.colorButtonInMain)
            )
        }

        IconButton(
            onClick = onTableClicked,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_table),
                contentDescription = "Table",
                tint = colorResource(id = R.color.colorButtonInMain)
            )
        }

        // Main layout using a Row and weights to simulate biases (20% and 80%)
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            
            // Left Content: Your Team (approx 20% bias)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.35f)
            ) {
                Text(
                    text = state.yourScore.toString(),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.colorRed)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.yourFouls.toString(),
                    fontSize = 35.sp,
                    color = colorResource(id = R.color.colorOrange),
                    modifier = Modifier.clickable { onYourFoulClicked() }.padding(4.dp)
                )
                Text(
                    text = "團犯",
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.colorButtonInMain)
                )
            }
            
            // Center Content: Quarter (at 50%)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.2f).padding(top = 40.dp)
            ) {
                Text(
                    text = state.quarter.toString(),
                    fontSize = 45.sp,
                    color = colorResource(id = R.color.colorOrange),
                    modifier = Modifier.clickable { onQuarterClicked() }.padding(4.dp)
                )
            }
            
            // Right Content: Opponent Team (approx 80% bias)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.35f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.opponentScore.toString(),
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.colorRed)
                    )
                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        IconButton(onClick = onOpponentScorePlus, modifier = Modifier.size(30.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus_box_white_24dp),
                                contentDescription = "Opponent Score Plus",
                                tint = colorResource(id = R.color.colorButtonInMain)
                            )
                        }
                        IconButton(onClick = onOpponentScoreMinus, modifier = Modifier.size(30.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_minus_check_box_white_24dp),
                                contentDescription = "Opponent Score Minus",
                                tint = colorResource(id = R.color.colorButtonInMain)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.opponentFouls.toString(),
                            fontSize = 35.sp,
                            color = colorResource(id = R.color.colorOrange)
                        )
                        Text(
                            text = "團犯",
                            fontSize = 15.sp,
                            color = colorResource(id = R.color.colorButtonInMain)
                        )
                    }
                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        IconButton(onClick = onOpponentFoulPlus, modifier = Modifier.size(30.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus_box_white_24dp),
                                contentDescription = "Opponent Foul Plus",
                                tint = colorResource(id = R.color.colorButtonInMain)
                            )
                        }
                        IconButton(onClick = onOpponentFoulMinus, modifier = Modifier.size(30.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_minus_check_box_white_24dp),
                                contentDescription = "Opponent Foul Minus",
                                tint = colorResource(id = R.color.colorButtonInMain)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(0.05f))
        }

        // Makeup Button (補登)
        TextButton(
            onClick = onMakeupClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .width(50.dp)
        ) {
            Text(
                text = "補登",
                fontSize = 15.sp,
                color = colorResource(id = R.color.colorButtonInMain)
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ScoreboardSectionPreview() {
    ScoreboardSection(
        state = GameUiState(
            yourScore = 85,
            opponentScore = 78,
            yourFouls = 3,
            opponentFouls = 4,
            quarter = 4
        )
    )
}
