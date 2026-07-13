package com.wadexhong.boxscore.gamehistory

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wadexhong.boxscore.database.entity.GameDataEntity

@Composable
fun GameBoxScoreSpreadsheet(gameDataList: List<GameDataEntity>) {
    val scrollState = rememberScrollState()
    
    // Simple placeholder for the 2D spreadsheet component
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(8.dp)
        ) {
            HeaderCell("Player")
            HeaderCell("PTS")
            HeaderCell("FGM")
            HeaderCell("FGA")
            HeaderCell("3PM")
            HeaderCell("3PA")
            HeaderCell("FTM")
            HeaderCell("FTA")
            HeaderCell("OREB")
            HeaderCell("DREB")
            HeaderCell("AST")
            HeaderCell("STL")
            HeaderCell("BLK")
            HeaderCell("TOV")
            HeaderCell("PF")
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(gameDataList) { data ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(8.dp)
                ) {
                    DataCell(data.playerName)
                    DataCell(data.pts.toString())
                    DataCell(data.fgm.toString())
                    DataCell(data.fga.toString())
                    DataCell(data.tpm.toString())
                    DataCell(data.tpa.toString())
                    DataCell(data.ftm.toString())
                    DataCell(data.fta.toString())
                    DataCell(data.oreb.toString())
                    DataCell(data.dreb.toString())
                    DataCell(data.ast.toString())
                    DataCell(data.stl.toString())
                    DataCell(data.blk.toString())
                    DataCell(data.tov.toString())
                    DataCell(data.pf.toString())
                }
                Divider()
            }
        }
    }
}

@Composable
fun HeaderCell(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(60.dp).padding(4.dp),
        maxLines = 1
    )
}

@Composable
fun DataCell(text: String) {
    Text(
        text = text,
        modifier = Modifier.width(60.dp).padding(4.dp),
        maxLines = 1
    )
}
