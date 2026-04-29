package com.nammamela.app.viewmodel;

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
public final class MyBookingsViewModel_Factory implements Factory<MyBookingsViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  public MyBookingsViewModel_Factory(Provider<AppRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MyBookingsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MyBookingsViewModel_Factory create(Provider<AppRepository> repositoryProvider) {
    return new MyBookingsViewModel_Factory(repositoryProvider);
  }

  public static MyBookingsViewModel newInstance(AppRepository repository) {
    return new MyBookingsViewModel(repository);
  }
}
