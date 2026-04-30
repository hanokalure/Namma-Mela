package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammamela.app.domain.model.Actor
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {
    @Query("SELECT * FROM actors WHERE playId = :playId")
    fun getActorsForPlay(playId: Int): Flow<List<Actor>>

    @Query("SELECT * FROM actors")
    fun getAllActors(): Flow<List<Actor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: Actor)

    @Update
    suspend fun updateActor(actor: Actor)

    @Delete
    suspend fun deleteActor(actor: Actor)

    @Query("DELETE FROM actors")
    suspend fun deleteAllActors()
}
