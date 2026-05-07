package com.nammamela.app.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val userIdKey = intPreferencesKey("logged_in_user_id")

    private val _isRestored = MutableStateFlow(false)
    val isRestored: StateFlow<Boolean> = _isRestored.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    val userIdFromStore: Flow<Int?> = dataStore.data.map { prefs ->
        val v = prefs[userIdKey] ?: 0
        if (v > 0) v else null
    }

    suspend fun restore() {
        if (_isRestored.value) return
        val id = dataStore.data.first()[userIdKey] ?: 0
        _userId.value = if (id > 0) id else null
        _isRestored.value = true
    }

    suspend fun setUserId(id: Int) {
        dataStore.edit { it[userIdKey] = id }
        _userId.value = id
    }

    suspend fun clear() {
        dataStore.edit { it.remove(userIdKey) }
        _userId.value = null
    }

    /** Call after [AppRepository.wipeDatabase] so login prefs match an empty DB and splash can proceed. */
    suspend fun clearSessionAfterDatabaseWipe() {
        dataStore.edit { it.remove(userIdKey) }
        _userId.value = null
        _isRestored.value = true
    }
}
