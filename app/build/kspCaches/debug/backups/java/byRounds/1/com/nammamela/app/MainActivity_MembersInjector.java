package com.nammamela.app;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.nammamela.app.data.session.UserSession;
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

  private final Provider<UserSession> userSessionProvider;

  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public MainActivity_MembersInjector(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userSessionProvider = userSessionProvider;
    this.dataStoreProvider = dataStoreProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<AppRepository> repositoryProvider,
      Provider<UserSession> userSessionProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new MainActivity_MembersInjector(repositoryProvider, userSessionProvider, dataStoreProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectRepository(instance, repositoryProvider.get());
    injectUserSession(instance, userSessionProvider.get());
    injectDataStore(instance, dataStoreProvider.get());
  }

  @InjectedFieldSignature("com.nammamela.app.MainActivity.repository")
  public static void injectRepository(MainActivity instance, AppRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.nammamela.app.MainActivity.userSession")
  public static void injectUserSession(MainActivity instance, UserSession userSession) {
    instance.userSession = userSession;
  }

  @InjectedFieldSignature("com.nammamela.app.MainActivity.dataStore")
  public static void injectDataStore(MainActivity instance, DataStore<Preferences> dataStore) {
    instance.dataStore = dataStore;
  }
}
