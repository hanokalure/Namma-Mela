package com.nammamela.app.util

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object AvatarPersistence {

    suspend fun copyAvatarToInternalStorage(
        appContext: Context,
        userId: Int,
        sourceUri: Uri
    ): String? = copyImageToInternalFile(appContext, "avatar_$userId", sourceUri)

    /** Saves under `[filesDir]/[baseName].jpg` — used for cast photos (`actor_12`) etc. */
    suspend fun copyImageToInternalFile(
        appContext: Context,
        baseName: String,
        sourceUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        try {
            val dest = File(appContext.filesDir, "$baseName.jpg")
            appContext.contentResolver.openInputStream(sourceUri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            } ?: return@withContext null
            dest.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    /** Copy an on-disk image (e.g. another actor’s photo) to `[filesDir]/[destBaseName].jpg`. */
    suspend fun copyImageFromExistingFile(
        appContext: Context,
        destBaseName: String,
        existingAbsolutePath: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val src = File(existingAbsolutePath)
            if (!src.exists()) return@withContext null
            val dest = File(appContext.filesDir, "$destBaseName.jpg")
            src.inputStream().use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            }
            dest.absolutePath
        } catch (_: Exception) {
            null
        }
    }
}
