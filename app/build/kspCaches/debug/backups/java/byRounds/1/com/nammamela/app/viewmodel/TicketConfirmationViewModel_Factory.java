package com.nammamela.app.viewmodel;

import androidx.lifecycle.SavedStateHandle;
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
public final class TicketConfirmationViewModel_Factory implements Factory<TicketConfirmationViewModel> {
  private final Provider<AppRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public TicketConfirmationViewModel_Factory(Provider<AppRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public TicketConfirmationViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static TicketConfirmationViewModel_Factory create(
      Provider<AppRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new TicketConfirmationViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static TicketConfirmationViewModel newInstance(AppRepository repository,
      SavedStateHandle savedStateHandle) {
    return new TicketConfirmationViewModel(repository, savedStateHandle);
  }
}
