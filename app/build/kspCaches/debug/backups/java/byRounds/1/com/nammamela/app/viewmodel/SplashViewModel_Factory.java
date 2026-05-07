package com.nammamela.app.viewmodel;

import com.nammamela.app.data.session.UserSession;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<UserSession> userSessionProvider;

  public SplashViewModel_Factory(Provider<UserSession> userSessionProvider) {
    this.userSessionProvider = userSessionProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(userSessionProvider.get());
  }

  public static SplashViewModel_Factory create(Provider<UserSession> userSessionProvider) {
    return new SplashViewModel_Factory(userSessionProvider);
  }

  public static SplashViewModel newInstance(UserSession userSession) {
    return new SplashViewModel(userSession);
  }
}
