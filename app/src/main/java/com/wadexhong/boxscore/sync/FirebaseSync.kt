package com.wadexhong.boxscore.sync

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.wadexhong.boxscore.Constants.FireBaseConstant
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * Firebase side of the Room data layer, replacing the cursor-based legacy
 * modelhelper.firebasemodel Create/Delete/Get. Everything is a no-op when signed out.
 */
object FirebaseSync {

    private const val TAG = "FirebaseSync"

    private val uid: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    /** Legacy Create.uploadExcelFile: uploads users/{uid}/xls_files/{gameId}.xls, returns its URL. */
    suspend fun uploadExcel(gameId: String, file: File): String? {
        val user = uid ?: return null
        return try {
            val ref = FirebaseStorage.getInstance().reference
                .child(FireBaseConstant.NODE_NAME_USERS)
                .child(user)
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
