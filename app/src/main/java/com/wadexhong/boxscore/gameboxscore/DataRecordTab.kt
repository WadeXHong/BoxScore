package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R

@Composable
fun DataRecordTab(
    onStatClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.Transparent)
    ) {
        val offensiveStats = listOf("2PM", "2PA", "3PM", "3PA", "FTM", "FTA", "OREB", "AST", "TO")
        val defensiveStats = listOf("DREB", "STL", "BLK", "PF")

        Text(
            text = "Offense",
            fontSize = 20.sp,
            color = colorResource(id = R.color.colorButtonInMain),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(offensiveStats) { stat ->
                StatButton(stat = stat, onClick = { onStatClicked(stat) })
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Defense",
            fontSize = 20.sp,
            color = colorResource(id = R.color.colorButtonInMain),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(defensiveStats) { stat ->
                StatButton(stat = stat, onClick = { onStatClicked(stat) })
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun DataRecordTabPreview() {
    DataRecordTab(
        onStatClicked = {}
    )
}

@Composable
fun StatButton(stat: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.colorDarkBackground),
            contentColor = colorResource(id = R.color.colorButtonInMain)
        )
    ) {
        Text(
            text = stat,
            fontSize = 20.sp,
            color = colorResource(id = R.color.colorButtonInMain)
        )
    }
}
