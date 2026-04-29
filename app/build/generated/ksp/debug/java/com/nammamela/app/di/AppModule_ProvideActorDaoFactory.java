package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.ActorDao;
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
public final class AppModule_ProvideActorDaoFactory implements Factory<ActorDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideActorDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ActorDao get() {
    return provideActorDao(dbProvider.get());
  }

  public static AppModule_ProvideActorDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideActorDaoFactory(dbProvider);
  }

  public static ActorDao provideActorDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideActorDao(db));
  }
}
