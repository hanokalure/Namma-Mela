package com.nammamela.app.data.repository;

import com.nammamela.app.data.local.dao.ActorDao;
import com.nammamela.app.data.local.dao.BookingDao;
import com.nammamela.app.data.local.dao.CategoryDao;
import com.nammamela.app.data.local.dao.CommentDao;
import com.nammamela.app.data.local.dao.NotificationDao;
import com.nammamela.app.data.local.dao.PlayDao;
import com.nammamela.app.data.local.dao.SeatDao;
import com.nammamela.app.data.local.dao.UserDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppRepositoryImpl_Factory implements Factory<AppRepositoryImpl> {
  private final Provider<PlayDao> playDaoProvider;

  private final Provider<ActorDao> actorDaoProvider;

  private final Provider<SeatDao> seatDaoProvider;

  private final Provider<CommentDao> commentDaoProvider;

  private final Provider<BookingDao> bookingDaoProvider;

  private final Provider<UserDao> userDaoProvider;

  private final Provider<NotificationDao> notificationDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public AppRepositoryImpl_Factory(Provider<PlayDao> playDaoProvider,
      Provider<ActorDao> actorDaoProvider, Provider<SeatDao> seatDaoProvider,
      Provider<CommentDao> commentDaoProvider, Provider<BookingDao> bookingDaoProvider,
      Provider<UserDao> userDaoProvider, Provider<NotificationDao> notificationDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    this.playDaoProvider = playDaoProvider;
    this.actorDaoProvider = actorDaoProvider;
    this.seatDaoProvider = seatDaoProvider;
    this.commentDaoProvider = commentDaoProvider;
    this.bookingDaoProvider = bookingDaoProvider;
    this.userDaoProvider = userDaoProvider;
    this.notificationDaoProvider = notificationDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public AppRepositoryImpl get() {
    return newInstance(playDaoProvider.get(), actorDaoProvider.get(), seatDaoProvider.get(), commentDaoProvider.get(), bookingDaoProvider.get(), userDaoProvider.get(), notificationDaoProvider.get(), categoryDaoProvider.get());
  }

  public static AppRepositoryImpl_Factory create(Provider<PlayDao> playDaoProvider,
      Provider<ActorDao> actorDaoProvider, Provider<SeatDao> seatDaoProvider,
      Provider<CommentDao> commentDaoProvider, Provider<BookingDao> bookingDaoProvider,
      Provider<UserDao> userDaoProvider, Provider<NotificationDao> notificationDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new AppRepositoryImpl_Factory(playDaoProvider, actorDaoProvider, seatDaoProvider, commentDaoProvider, bookingDaoProvider, userDaoProvider, notificationDaoProvider, categoryDaoProvider);
  }

  public static AppRepositoryImpl newInstance(PlayDao playDao, ActorDao actorDao, SeatDao seatDao,
      CommentDao commentDao, BookingDao bookingDao, UserDao userDao,
      NotificationDao notificationDao, CategoryDao categoryDao) {
    return new AppRepositoryImpl(playDao, actorDao, seatDao, commentDao, bookingDao, userDao, notificationDao, categoryDao);
  }
}
