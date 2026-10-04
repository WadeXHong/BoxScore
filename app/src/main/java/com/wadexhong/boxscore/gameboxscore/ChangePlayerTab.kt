package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun ChangePlayerTab(
    onCourtPlayers: List<TeamPlayerEntity>,
    onPlayerClicked: (TeamPlayerEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDarkBackground))
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(onCourtPlayers) { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 5.dp)
                        .background(colorResource(id = R.color.colorButtonInMain))
                        .clickable { onPlayerClicked(player) }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = player.playerNumber,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.colorTextInMainButton),
                        modifier = Modifier.width(50.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = player.playerName,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.colorTextInMainButton)
                    )
                }
            }
        }
    }
}
