package com.wadexhong.boxscore.gamehistory

import com.wadexhong.boxscore.Constants.GameDataDBContract
import com.wadexhong.boxscore.Constants.GameInfoDBContract
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import java.io.File
import java.util.Locale

/**
 * Port of legacy CreateExcelTask: a GameData sheet (every player/quarter row) and a GameInfo
 * sheet. Legacy wrote to external storage, which needs a permission and is no longer writable
 * on current Android versions, so the file goes to [directory] (the app cache) instead.
 */
object GameExcelExporter {

    private val DATA_HEADERS = listOf(
        GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER, GameDataDBContract.COLUMN_NAME_PLAYER_NAME,
        GameDataDBContract.COLUMN_NAME_QUARTER
    ) + HistoryStats.STAT_COLUMNS

    private val INFO_HEADERS = listOf(
        GameInfoDBContract.COLUMN_NAME_YOUR_TEAM, GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME,
        GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE, GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE,
        GameInfoDBContract.COLUMN_NAME_GAME_DATE
    )

    /** Returns the written file, or null when the game has no data (legacy onError). */
    fun export(directory: File, info: GameInfoEntity, rows: List<GameDataEntity>): File? {
        if (rows.isEmpty()) return null
        directory.mkdirs()
        val file = File(directory, "${info.gameId}.xls")

        val workbook = Workbook.createWorkbook(file, WorkbookSettings().apply { locale = Locale("en", "EN") })
        try {
            val dataSheet = workbook.createSheet("GameData", 0)
            val infoSheet = workbook.createSheet("GameInfo", 1)

            DATA_HEADERS.forEachIndexed { col, title -> dataSheet.addCell(Label(col, 0, title)) }
            rows.forEachIndexed { index, row ->
                val cells = listOf(row.playerNumber, row.playerName, row.quarter.toString()) +
                    HistoryStats.values(row).map { it.toString() }
                cells.forEachIndexed { col, value -> dataSheet.addCell(Label(col, index + 1, value)) }
            }

            INFO_HEADERS.forEachIndexed { col, title -> infoSheet.addCell(Label(col, 0, title)) }
            listOf(
                info.yourTeamName, info.opponentName, info.yourScore.toString(),
                info.opponentScore.toString(), info.gameDate
            ).forEachIndexed { col, value -> infoSheet.addCell(Label(col, 1, value)) }

            workbook.write()
        } finally {
            workbook.close()
        }
        return file
    }
}
