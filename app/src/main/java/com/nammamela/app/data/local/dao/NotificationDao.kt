package com.nammamela.app.data.local.dao

import androidx.room.*
import com.nammamela.app.domain.model.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Delete
    suspend fun deleteNotification(notification: Notification)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
}
