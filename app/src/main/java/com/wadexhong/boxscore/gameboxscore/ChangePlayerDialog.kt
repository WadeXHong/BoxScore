package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun ChangePlayerDialog(
    onDismissRequest: () -> Unit,
    benchPlayers: List<TeamPlayerEntity>,
    onPlayerSelected: (TeamPlayerEntity) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.colorDarkBackground), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Select Bench Player",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                color = colorResource(id = R.color.colorTextInMainButton)
            )
            
            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                items(benchPlayers) { player ->
                    Text(
                        text = "${player.playerNumber} - ${player.playerName}",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.colorTextInMainButton),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onPlayerSelected(player)
                                onDismissRequest()
                            }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}
