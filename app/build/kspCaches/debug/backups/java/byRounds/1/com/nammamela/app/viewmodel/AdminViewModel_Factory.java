package com.nammamela.app.viewmodel;

import android.content.Context;
import com.nammamela.app.data.session.UserSession;
import com.nammamela.app.domain.repository.AppRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AdminViewModel_Factory implements Factory<AdminViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<UserSession> userSessionProvider;

  private final Provider<Context> appContextProvider;

  public AdminViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider, Provider<Context> appContextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userSessionProvider = userSessionProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(repositoryProvider.get(), userSessionProvider.get(), appContextProvider.get());
  }

  public static AdminViewModel_Factory create(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider, Provider<Context> appContextProvider) {
    return new AdminViewModel_Factory(repositoryProvider, userSessionProvider, appContextProvider);
  }

  public static AdminViewModel newInstance(AppRepository repository, UserSession userSession,
      Context appContext) {
    return new AdminViewModel(repository, userSession, appContext);
  }
}
