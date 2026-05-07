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
public final class NotificationViewModel_Factory implements Factory<NotificationViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<UserSession> userSessionProvider;

  public NotificationViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userSessionProvider = userSessionProvider;
  }

  @Override
  public NotificationViewModel get() {
    return newInstance(repositoryProvider.get(), userSessionProvider.get());
  }

  public static NotificationViewModel_Factory create(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider) {
    return new NotificationViewModel_Factory(repositoryProvider, userSessionProvider);
  }

  public static NotificationViewModel newInstance(AppRepository repository,
      UserSession userSession) {
    return new NotificationViewModel(repository, userSession);
  }
}
