package com.nammamela.app.viewmodel;

import androidx.lifecycle.SavedStateHandle;
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
public final class ReviewViewModel_Factory implements Factory<ReviewViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<UserSession> userSessionProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ReviewViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userSessionProvider = userSessionProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ReviewViewModel get() {
    return newInstance(repositoryProvider.get(), userSessionProvider.get(), savedStateHandleProvider.get());
  }

  public static ReviewViewModel_Factory create(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ReviewViewModel_Factory(repositoryProvider, userSessionProvider, savedStateHandleProvider);
  }

  public static ReviewViewModel newInstance(AppRepository repository, UserSession userSession,
      SavedStateHandle savedStateHandle) {
    return new ReviewViewModel(repository, userSession, savedStateHandle);
  }
}
