package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.PlayDao;
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
public final class AppModule_ProvidePlayDaoFactory implements Factory<PlayDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvidePlayDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PlayDao get() {
    return providePlayDao(dbProvider.get());
  }

  public static AppModule_ProvidePlayDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvidePlayDaoFactory(dbProvider);
  }

  public static PlayDao providePlayDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePlayDao(db));
  }
}
