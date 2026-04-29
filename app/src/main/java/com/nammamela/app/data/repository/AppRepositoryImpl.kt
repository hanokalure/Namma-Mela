package com.nammamela.app.data.repository

import com.nammamela.app.data.local.dao.*
import com.nammamela.app.domain.model.*
import com.nammamela.app.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val playDao: PlayDao,
    private val actorDao: ActorDao,
    private val seatDao: SeatDao,
    private val commentDao: CommentDao,
    private val bookingDao: BookingDao,
    private val userDao: UserDao
) : AppRepository {

    override fun getAllPlays(): Flow<List<Play>> = playDao.getAllPlays()
    override fun getPlayById(id: Int): Flow<Play?> = playDao.getPlayById(id)
    override fun getActivePlay(): Flow<Play?> = playDao.getActivePlay()
    override suspend fun archiveAllPlays() = playDao.archiveAllPlays()
    override suspend fun insertPlay(play: Play): Long = playDao.insertPlay(play)
    override suspend fun updatePlay(play: Play) = playDao.updatePlay(play)

    override fun getActorsForPlay(playId: Int): Flow<List<Actor>> = actorDao.getActorsForPlay(playId)
    override fun getAllActors(): Flow<List<Actor>> = actorDao.getAllActors()
    override suspend fun insertActor(actor: Actor) = actorDao.insertActor(actor)
    override suspend fun updateActor(actor: Actor) = actorDao.updateActor(actor)
    override suspend fun deleteActor(actor: Actor) = actorDao.deleteActor(actor)

    override fun getSeatsForPlay(playId: Int): Flow<List<Seat>> = seatDao.getSeatsForPlay(playId)
    override suspend fun insertSeats(seats: List<Seat>) = seatDao.insertSeats(seats)
    override suspend fun updateSeat(seat: Seat) = seatDao.updateSeat(seat)
    override suspend fun updateSeats(seats: List<Seat>) = seatDao.updateSeats(seats)
    override suspend fun deleteSeatsForPlay(playId: Int) = seatDao.deleteSeatsForPlay(playId)

    override fun getAllComments(): Flow<List<Comment>> = commentDao.getAllComments()
    override suspend fun insertComment(comment: Comment) = commentDao.insertComment(comment)
    override suspend fun updateComment(comment: Comment) = commentDao.updateComment(comment)
    override suspend fun deleteComment(comment: Comment) = commentDao.deleteComment(comment)

    override fun getBookingsForUser(userId: Int): Flow<List<Booking>> = bookingDao.getBookingsForUser(userId)
    override fun getBookingsWithPlayForUser(userId: Int): Flow<List<BookingWithPlay>> = bookingDao.getBookingsWithPlayForUser(userId)
    override fun getBookingWithPlayById(bookingId: Int): Flow<BookingWithPlay?> = bookingDao.getBookingWithPlayById(bookingId)
    override suspend fun insertBooking(booking: Booking): Long = bookingDao.insertBooking(booking)

    override fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)
    override suspend fun insertUser(user: User) = userDao.insertUser(user)
}
