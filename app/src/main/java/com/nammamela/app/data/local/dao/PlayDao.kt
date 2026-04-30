package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammamela.app.domain.model.Play
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayDao {
    @Query("SELECT * FROM plays ORDER BY timestamp DESC")
    fun getAllPlays(): Flow<List<Play>>

    @Query("SELECT * FROM plays")
    suspend fun getAllPlaysOnce(): List<Play>

    @Query("SELECT * FROM plays WHERE id = :id")
    fun getPlayById(id: Int): Flow<Play?>

    @Query("SELECT * FROM plays WHERE id = :id")
    suspend fun getPlayByIdOnce(id: Int): Play?

    @Query("SELECT * FROM plays WHERE isActive = 1 ORDER BY timestamp DESC LIMIT 1")
    fun getActivePlay(): Flow<Play?>

    @Query("UPDATE plays SET isActive = 0")
    suspend fun archiveAllPlays()

    @Query("DELETE FROM plays")
    suspend fun deleteAllPlays()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlay(play: Play): Long

    @Update
    suspend fun updatePlay(play: Play)
}
