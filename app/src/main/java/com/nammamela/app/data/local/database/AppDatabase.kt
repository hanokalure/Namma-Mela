package com.nammamela.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nammamela.app.data.local.dao.*
import com.nammamela.app.domain.model.*

@Database(
    entities = [Play::class, Actor::class, Seat::class, Comment::class, Booking::class, User::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val playDao: PlayDao
    abstract val actorDao: ActorDao
    abstract val seatDao: SeatDao
    abstract val commentDao: CommentDao
    abstract val bookingDao: BookingDao
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "nammamela_db"
    }
}
