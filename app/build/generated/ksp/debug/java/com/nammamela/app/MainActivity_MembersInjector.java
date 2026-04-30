package com.nammamela.app;

import com.nammamela.app.domain.repository.AppRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<AppRepository> repositoryProvider;

  public MainActivity_MembersInjector(Provider<AppRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<AppRepository> repositoryProvider) {
    return new MainActivity_MembersInjector(repositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.nammamela.app.MainActivity.repository")
  public static void injectRepository(MainActivity instance, AppRepository repository) {
    instance.repository = repository;
  }
}
