package com.nammamela.app.di

import android.content.Context
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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideAppRepository(
        playDao: PlayDao,
        actorDao: ActorDao,
        seatDao: SeatDao,
        commentDao: CommentDao,
        bookingDao: BookingDao,
        userDao: UserDao
    ): AppRepository {
        return AppRepositoryImpl(playDao, actorDao, seatDao, commentDao, bookingDao, userDao)
    }
}
