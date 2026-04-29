package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammamela.app.domain.model.Seat
import kotlinx.coroutines.flow.Flow

@Dao
interface SeatDao {
    @Query("SELECT * FROM seats WHERE playId = :playId")
    fun getSeatsForPlay(playId: Int): Flow<List<Seat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeats(seats: List<Seat>)

    @Update
    suspend fun updateSeat(seat: Seat)
    
    @Update
    suspend fun updateSeats(seats: List<Seat>)

    @Query("DELETE FROM seats WHERE playId = :playId")
    suspend fun deleteSeatsForPlay(playId: Int)
}
