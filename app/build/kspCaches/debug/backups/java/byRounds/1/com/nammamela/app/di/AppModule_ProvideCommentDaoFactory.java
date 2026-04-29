package com.nammamela.app.di;

import com.nammamela.app.data.local.dao.CommentDao;
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
public final class AppModule_ProvideCommentDaoFactory implements Factory<CommentDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideCommentDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CommentDao get() {
    return provideCommentDao(dbProvider.get());
  }

  public static AppModule_ProvideCommentDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideCommentDaoFactory(dbProvider);
  }

  public static CommentDao provideCommentDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCommentDao(db));
  }
}
