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
public final class FanWallViewModel_Factory implements Factory<FanWallViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  public FanWallViewModel_Factory(Provider<AppRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public FanWallViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static FanWallViewModel_Factory create(Provider<AppRepository> repositoryProvider) {
    return new FanWallViewModel_Factory(repositoryProvider);
  }

  public static FanWallViewModel newInstance(AppRepository repository) {
    return new FanWallViewModel(repository);
  }
}
