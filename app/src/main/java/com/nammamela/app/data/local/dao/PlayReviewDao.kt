package com.nammamela.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammamela.app.domain.model.PlayReview
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayReviewDao {
    @Query("SELECT * FROM play_reviews WHERE playId = :playId")
    suspend fun getReviewsForPlay(playId: Int): List<PlayReview>

    @Query("SELECT COUNT(*) FROM play_reviews WHERE userId = :userId")
    fun getReviewCountForUser(userId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: PlayReview): Long

    @Query("DELETE FROM play_reviews")
    suspend fun deleteAllReviews()

    @Query("DELETE FROM play_reviews WHERE playId = :playId")
    suspend fun deleteReviewsForPlay(playId: Int)
}
