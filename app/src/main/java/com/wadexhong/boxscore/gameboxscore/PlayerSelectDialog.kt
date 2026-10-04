package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun PlayerSelectDialog(
    onDismissRequest: () -> Unit,
    players: List<TeamPlayerEntity>,
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
                text = "Select Player",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                color = colorResource(id = R.color.colorTextInMainButton)
            )
            
            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                items(players) { player ->
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
