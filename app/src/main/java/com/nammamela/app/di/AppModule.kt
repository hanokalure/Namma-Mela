package com.nammamela.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.nammamela.app.data.local.dao.*
import com.nammamela.app.data.local.database.AppDatabase
import com.nammamela.app.data.repository.AppRepositoryImpl
import com.nammamela.app.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_session"
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.sessionDataStore
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePlayDao(db: AppDatabase): PlayDao = db.playDao

    @Provides
    @Singleton
    fun provideActorDao(db: AppDatabase): ActorDao = db.actorDao

    @Provides
    @Singleton
    fun provideSeatDao(db: AppDatabase): SeatDao = db.seatDao

    @Provides
    @Singleton
    fun provideCommentDao(db: AppDatabase): CommentDao = db.commentDao

    @Provides
    @Singleton
    fun provideBookingDao(db: AppDatabase): BookingDao = db.bookingDao

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao

    @Provides
    @Singleton
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao

    @Provides
    @Singleton
    fun providePlayReviewDao(db: AppDatabase): PlayReviewDao = db.playReviewDao

    @Provides
    @Singleton
    fun provideAppRepository(
        playDao: PlayDao,
        actorDao: ActorDao,
        seatDao: SeatDao,
        commentDao: CommentDao,
        bookingDao: BookingDao,
        userDao: UserDao,
        notificationDao: NotificationDao,
        categoryDao: CategoryDao,
        playReviewDao: PlayReviewDao
    ): AppRepository {
        return AppRepositoryImpl(
            playDao,
            actorDao,
            seatDao,
            commentDao,
            bookingDao,
            userDao,
            notificationDao,
            categoryDao,
            playReviewDao
        )
    }
}
