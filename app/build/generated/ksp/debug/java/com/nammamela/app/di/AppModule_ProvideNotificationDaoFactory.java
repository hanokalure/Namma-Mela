package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.NotificationDao;
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
public final class AppModule_ProvideNotificationDaoFactory implements Factory<NotificationDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideNotificationDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public NotificationDao get() {
    return provideNotificationDao(dbProvider.get());
  }

  public static AppModule_ProvideNotificationDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideNotificationDaoFactory(dbProvider);
  }

  public static NotificationDao provideNotificationDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideNotificationDao(db));
  }
}
