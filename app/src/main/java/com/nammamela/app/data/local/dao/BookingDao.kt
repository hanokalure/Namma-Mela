package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nammamela.app.domain.model.Booking
import com.nammamela.app.domain.model.BookingWithPlay
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY timestamp DESC")
    fun getBookingsForUser(userId: Int): Flow<List<Booking>>

    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Transaction
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY timestamp DESC")
    fun getBookingsWithPlayForUser(userId: Int): Flow<List<BookingWithPlay>>

    @Transaction
    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    fun getBookingWithPlayById(bookingId: Int): Flow<BookingWithPlay?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking): Long

    @Query("DELETE FROM bookings")
    suspend fun deleteAllBookings()
}
