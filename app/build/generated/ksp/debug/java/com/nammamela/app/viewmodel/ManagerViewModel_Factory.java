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
public final class ManagerViewModel_Factory implements Factory<ManagerViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  public ManagerViewModel_Factory(Provider<AppRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ManagerViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ManagerViewModel_Factory create(Provider<AppRepository> repositoryProvider) {
    return new ManagerViewModel_Factory(repositoryProvider);
  }

  public static ManagerViewModel newInstance(AppRepository repository) {
    return new ManagerViewModel(repository);
  }
}
