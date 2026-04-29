package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.SeatDao;
import com.nammamela.app.data.local.database.AppDatabase;
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
public final class AppModule_ProvideSeatDaoFactory implements Factory<SeatDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideSeatDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SeatDao get() {
    return provideSeatDao(dbProvider.get());
  }

  public static AppModule_ProvideSeatDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideSeatDaoFactory(dbProvider);
  }

  public static SeatDao provideSeatDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSeatDao(db));
  }
}
