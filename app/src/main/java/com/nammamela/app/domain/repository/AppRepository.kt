package com.nammamela.app.domain.repository

import com.nammamela.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    // Play
    fun getAllPlays(): Flow<List<Play>>
    fun getPlayById(id: Int): Flow<Play?>
    suspend fun getPlayByIdOnce(id: Int): Play?
    fun getActivePlay(): Flow<Play?>
    suspend fun archiveAllPlays()
    suspend fun insertPlay(play: Play): Long
    suspend fun updatePlay(play: Play)
    suspend fun deletePlayCascade(playId: Int)

    // Actor
    fun getActorsForPlay(playId: Int): Flow<List<Actor>>
    fun getAllActors(): Flow<List<Actor>>
    suspend fun insertActor(actor: Actor): Long
    suspend fun updateActor(actor: Actor)
    suspend fun deleteActor(actor: Actor)

    // Seat
    fun getSeatsForPlay(playId: Int): Flow<List<Seat>>
    suspend fun insertSeats(seats: List<Seat>)
    suspend fun updateSeat(seat: Seat)
    suspend fun updateSeats(seats: List<Seat>)
    suspend fun deleteSeatsForPlay(playId: Int)

    // Comment
    fun getAllComments(): Flow<List<Comment>>
    suspend fun insertComment(comment: Comment)
    suspend fun updateComment(comment: Comment)
    suspend fun deleteComment(comment: Comment)

    // Booking
    fun getBookingsForUser(userId: Int): Flow<List<Booking>>
    fun getBookingsForPlay(playId: Int): Flow<List<Booking>>
    fun getAllBookings(): Flow<List<Booking>>
    fun getBookingsWithPlayForUser(userId: Int): Flow<List<BookingWithPlay>>
    fun getBookingWithPlayById(bookingId: Int): Flow<BookingWithPlay?>
    suspend fun insertBooking(booking: Booking): Long

    // User
    fun getUserById(userId: Int): Flow<User?>
    suspend fun getUserByIdOnce(userId: Int): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)

    // Notifications
    fun getAllNotifications(): Flow<List<Notification>>
    fun getNotificationsForUser(userId: Int): Flow<List<Notification>>
    suspend fun insertNotification(notification: Notification)
    suspend fun markNotificationAsRead(id: Int)
    suspend fun deleteNotification(notification: Notification)

    // Play reviews
    fun getReviewCountForUser(userId: Int): Flow<Int>
    suspend fun submitPlayReview(playId: Int, userId: Int, rating: Int, text: String)

    // Fan wall / profile stats
    fun getCommentCountForUser(userId: Int): Flow<Int>

    // Category
    fun getAllCategories(): Flow<List<Category>>
    suspend fun insertCategories(categories: List<Category>)

    // Rating
    suspend fun updatePlayRating(playId: Int, newRating: Float)

    suspend fun wipeDatabase()
    suspend fun seedDatabase()
}
