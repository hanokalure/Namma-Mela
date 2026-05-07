package com.nammamela.app.data.session;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class UserSession_Factory implements Factory<UserSession> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public UserSession_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public UserSession get() {
    return newInstance(dataStoreProvider.get());
  }

  public static UserSession_Factory create(Provider<DataStore<Preferences>> dataStoreProvider) {
    return new UserSession_Factory(dataStoreProvider);
  }

  public static UserSession newInstance(DataStore<Preferences> dataStore) {
    return new UserSession(dataStore);
  }
}
