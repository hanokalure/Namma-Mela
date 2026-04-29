package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.ActorDao;
import com.nammamela.app.data.local.dao.BookingDao;
import com.nammamela.app.data.local.dao.CommentDao;
import com.nammamela.app.data.local.dao.PlayDao;
import com.nammamela.app.data.local.dao.SeatDao;
import com.nammamela.app.data.local.dao.UserDao;
import com.nammamela.app.domain.repository.AppRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideAppRepositoryFactory implements Factory<AppRepository> {
  private final Provider<PlayDao> playDaoProvider;

  private final Provider<ActorDao> actorDaoProvider;

  private final Provider<SeatDao> seatDaoProvider;

  private final Provider<CommentDao> commentDaoProvider;

  private final Provider<BookingDao> bookingDaoProvider;

  private final Provider<UserDao> userDaoProvider;

  public AppModule_ProvideAppRepositoryFactory(Provider<PlayDao> playDaoProvider,
      Provider<ActorDao> actorDaoProvider, Provider<SeatDao> seatDaoProvider,
      Provider<CommentDao> commentDaoProvider, Provider<BookingDao> bookingDaoProvider,
      Provider<UserDao> userDaoProvider) {
    this.playDaoProvider = playDaoProvider;
    this.actorDaoProvider = actorDaoProvider;
    this.seatDaoProvider = seatDaoProvider;
    this.commentDaoProvider = commentDaoProvider;
    this.bookingDaoProvider = bookingDaoProvider;
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public AppRepository get() {
    return provideAppRepository(playDaoProvider.get(), actorDaoProvider.get(), seatDaoProvider.get(), commentDaoProvider.get(), bookingDaoProvider.get(), userDaoProvider.get());
  }

  public static AppModule_ProvideAppRepositoryFactory create(Provider<PlayDao> playDaoProvider,
      Provider<ActorDao> actorDaoProvider, Provider<SeatDao> seatDaoProvider,
      Provider<CommentDao> commentDaoProvider, Provider<BookingDao> bookingDaoProvider,
      Provider<UserDao> userDaoProvider) {
    return new AppModule_ProvideAppRepositoryFactory(playDaoProvider, actorDaoProvider, seatDaoProvider, commentDaoProvider, bookingDaoProvider, userDaoProvider);
  }

  public static AppRepository provideAppRepository(PlayDao playDao, ActorDao actorDao,
      SeatDao seatDao, CommentDao commentDao, BookingDao bookingDao, UserDao userDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAppRepository(playDao, actorDao, seatDao, commentDao, bookingDao, userDao));
  }
}
