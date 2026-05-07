package com.nammamela.app.viewmodel;

import com.nammamela.app.data.session.UserSession;
import com.nammamela.app.domain.repository.AppRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<UserSession> userSessionProvider;

  public AuthViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userSessionProvider = userSessionProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(repositoryProvider.get(), userSessionProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider) {
    return new AuthViewModel_Factory(repositoryProvider, userSessionProvider);
  }

  public static AuthViewModel newInstance(AppRepository repository, UserSession userSession) {
    return new AuthViewModel(repository, userSession);
  }
}
