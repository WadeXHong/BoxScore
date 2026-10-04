package com.wadexhong.boxscore.sync

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.wadexhong.boxscore.Constants.FireBaseConstant
import com.wadexhong.boxscore.Constants.TeamPlayersContract
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * Firebase side of the Room data layer, replacing the cursor-based legacy
 * modelhelper.firebasemodel Create/Delete/Get. Every call is a no-op when signed out.
 *
 * Writes are fire-and-forget like legacy: the Realtime Database client queues them while
 * offline. Reads and uploads that need a result are awaited.
 */
object FirebaseSync {

    private const val TAG = "FirebaseSync"

    /** users/{uid}, or null when nobody is signed in. */
    private fun userRef(): DatabaseReference? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        return FirebaseDatabase.getInstance().reference.child(FireBaseConstant.NODE_NAME_USERS).child(uid)
    }

    // --- Games ---

    /** Legacy createGameData + createGameInfo + updateTeamHistoryAmount for a finished game. */
    suspend fun uploadFinishedGame(db: BoxScoreDatabase, gameId: String) {
        val ref = userRef() ?: return
        val info = db.gameDao().getGameInfoById(gameId) ?: return
        val rows = db.gameDao().getGameDataList(gameId)
        val historyAmount = db.gameDao().countFinishedGames(info.yourTeamId)
        ref.updateChildren(FirebaseMapper.gameUpload(info, rows, historyAmount))
    }

    /** Legacy Delete.deleteGame. */
    fun deleteGame(teamId: String, gameId: String, newHistoryAmount: Int) {
        val ref = userRef() ?: return
        ref.child(FireBaseConstant.NODE_NAME_GAME_INFO).child(gameId).removeValue()
        ref.child(FirebaseMapper.teamHistoryPath(teamId)).setValue(newHistoryAmount)
    }

    // --- Teams and players ---

    /** Legacy Create.createTeam. */
    fun createTeam(team: TeamInfoEntity) {
        val ref = userRef() ?: return
        ref.child(FireBaseConstant.NODE_NAME_TEAM_INFO).child(team.teamId).setValue(FirebaseMapper.teamValues(team))
    }

    /** Legacy Delete.deleteTeam: the team and every player that belongs to it. */
    suspend fun deleteTeam(teamId: String) {
        val ref = userRef() ?: return
        ref.child(FireBaseConstant.NODE_NAME_TEAM_INFO).child(teamId).removeValue()
        try {
            ref.child(FireBaseConstant.NODE_NAME_TEAM_PLAYER)
                .orderByChild(TeamPlayersContract.COLUMN_NAME_TEAM_ID)
                .equalTo(teamId)
                .get().await()
                .children.forEach { it.ref.removeValue() }
        } catch (e: Exception) {
            Log.w(TAG, "deleteTeam: could not remove players of $teamId", e)
        }
    }

    /** Legacy Create.createPlayer. */
    fun createPlayer(player: TeamPlayerEntity, newPlayersAmount: Int) {
        val ref = userRef() ?: return
        ref.child(FireBaseConstant.NODE_NAME_TEAM_PLAYER).child(player.playerId).setValue(FirebaseMapper.playerValues(player))
        ref.child(FirebaseMapper.teamPlayersAmountPath(player.teamId)).setValue(newPlayersAmount)
    }

    /** Legacy Delete.deletePlayer. */
    fun deletePlayer(teamId: String, playerId: String, newPlayersAmount: Int) {
        val ref = userRef() ?: return
        ref.child(FireBaseConstant.NODE_NAME_TEAM_PLAYER).child(playerId).removeValue()
        ref.child(FirebaseMapper.teamPlayersAmountPath(teamId)).setValue(newPlayersAmount)
    }

    // --- Download ---

    /** Legacy Get.onCreate: pull users/{uid} into Room after login / app start. */
    suspend fun download(db: BoxScoreDatabase) {
        val ref = userRef() ?: return
        try {
            val snapshot = ref.get().await()
            val importer = RemoteDataImporter(db.gameDao(), db.teamDao(), db.playerDao(), GameRepository(db))
            importer.import(FirebaseMapper.parseUser(snapshot.value))
        } catch (e: Exception) {
            Log.w(TAG, "download failed", e)
        }
    }

    // --- Storage ---

    /** Legacy Create.uploadExcelFile: uploads users/{uid}/xls_files/{gameId}.xls, returns its URL. */
    suspend fun uploadExcel(gameId: String, file: File): String? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        return try {
            val ref = FirebaseStorage.getInstance().reference
                .child(FireBaseConstant.NODE_NAME_USERS)
                .child(uid)
                .child(FireBaseConstant.NODE_NAME_XLS_FILES)
                .child("$gameId.xls")
            ref.putFile(Uri.fromFile(file)).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.w(TAG, "uploadExcel failed", e)
            null
        }
    }
}
