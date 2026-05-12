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
    private val userDao: UserDao,
    private val notificationDao: NotificationDao,
    private val categoryDao: CategoryDao,
    private val playReviewDao: PlayReviewDao
) : AppRepository {

    override fun getAllPlays(): Flow<List<Play>> = playDao.getAllPlays()
    override fun getPlayById(id: Int): Flow<Play?> = playDao.getPlayById(id)
    override suspend fun getPlayByIdOnce(id: Int): Play? = playDao.getPlayByIdOnce(id)
    override fun getActivePlay(): Flow<Play?> = playDao.getActivePlay()
    override suspend fun archiveAllPlays() = playDao.archiveAllPlays()
    override suspend fun insertPlay(play: Play): Long = playDao.insertPlay(play)
    override suspend fun updatePlay(play: Play) = playDao.updatePlay(play)

    override suspend fun deletePlayCascade(playId: Int) {
        playReviewDao.deleteReviewsForPlay(playId)
        bookingDao.deleteBookingsForPlay(playId)
        seatDao.deleteSeatsForPlay(playId)
        actorDao.deleteActorsForPlay(playId)
        playDao.deletePlayById(playId)
    }

    override fun getActorsForPlay(playId: Int): Flow<List<Actor>> = actorDao.getActorsForPlay(playId)
    override fun getAllActors(): Flow<List<Actor>> = actorDao.getAllActors()
    override suspend fun insertActor(actor: Actor): Long = actorDao.insertActor(actor)
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
    override fun getBookingsForPlay(playId: Int): Flow<List<Booking>> = bookingDao.getBookingsForPlay(playId)
    override fun getAllBookings(): Flow<List<Booking>> = bookingDao.getAllBookings()
    override fun getBookingsWithPlayForUser(userId: Int): Flow<List<BookingWithPlay>> = bookingDao.getBookingsWithPlayForUser(userId)
    override fun getBookingWithPlayById(bookingId: Int): Flow<BookingWithPlay?> = bookingDao.getBookingWithPlayById(bookingId)
    override suspend fun insertBooking(booking: Booking): Long = bookingDao.insertBooking(booking)

    override fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)
    override suspend fun getUserByIdOnce(userId: Int): User? = userDao.getUserByIdOnce(userId)
    override suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    override suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    override suspend fun updateUser(user: User) = userDao.updateUser(user)

    override fun getAllNotifications(): Flow<List<Notification>> = notificationDao.getAllNotifications()
    override fun getNotificationsForUser(userId: Int): Flow<List<Notification>> =
        notificationDao.getNotificationsForUser(userId)

    override suspend fun insertNotification(notification: Notification) = notificationDao.insertNotification(notification)
    override suspend fun markNotificationAsRead(id: Int) = notificationDao.markAsRead(id)
    override suspend fun deleteNotification(notification: Notification) = notificationDao.deleteNotification(notification)

    override fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    override suspend fun insertCategories(categories: List<Category>) = categoryDao.insertCategories(categories)

    override fun getReviewCountForUser(userId: Int): Flow<Int> = playReviewDao.getReviewCountForUser(userId)

    override suspend fun submitPlayReview(playId: Int, userId: Int, rating: Int, text: String) {
        playReviewDao.insert(
            PlayReview(playId = playId, userId = userId, rating = rating, text = text)
        )
        val reviews = playReviewDao.getReviewsForPlay(playId)
        val avg = if (reviews.isEmpty()) 0f else reviews.map { it.rating }.average().toFloat()
        updatePlayRating(playId, avg)
    }

    override fun getCommentCountForUser(userId: Int): Flow<Int> = commentDao.getCommentCountForUser(userId)

    override suspend fun updatePlayRating(playId: Int, newRating: Float) {
        playDao.getPlayByIdOnce(playId)?.let { play ->
            playDao.updatePlay(play.copy(rating = newRating))
        }
    }

    override suspend fun wipeDatabase() {
        playReviewDao.deleteAllReviews()
        seatDao.deleteAllSeats()
        playDao.deleteAllPlays()
        actorDao.deleteAllActors()
        userDao.deleteAllUsers()
        bookingDao.deleteAllBookings()
        commentDao.deleteAllComments()
        notificationDao.deleteAllNotifications()
        categoryDao.deleteAllCategories()
    }

    override suspend fun seedDatabase() {
        if (categoryDao.getAllCategoriesOnce().isNotEmpty()) return
        insertCategories(
            listOf(
                Category(name = "Drama"),
                Category(name = "Music"),
                Category(name = "Comedy"),
                Category(name = "Musical"),
                Category(name = "Classical")
            )
        )
    }
}
