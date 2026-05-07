package com.nammamela.app.viewmodel;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
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
public final class ManageCastViewModel_Factory implements Factory<ManageCastViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<Context> appContextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ManageCastViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<Context> appContextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.appContextProvider = appContextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ManageCastViewModel get() {
    return newInstance(repositoryProvider.get(), appContextProvider.get(), savedStateHandleProvider.get());
  }

  public static ManageCastViewModel_Factory create(Provider<AppRepository> repositoryProvider,
      Provider<Context> appContextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ManageCastViewModel_Factory(repositoryProvider, appContextProvider, savedStateHandleProvider);
  }

  public static ManageCastViewModel newInstance(AppRepository repository, Context appContext,
      SavedStateHandle savedStateHandle) {
    return new ManageCastViewModel(repository, appContext, savedStateHandle);
  }
}
