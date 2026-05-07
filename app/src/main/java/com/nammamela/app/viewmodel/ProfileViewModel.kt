package com.nammamela.app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.Booking
import com.nammamela.app.domain.model.User
import com.nammamela.app.domain.repository.AppRepository
import com.nammamela.app.util.AvatarPersistence
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userSession: UserSession,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount.asStateFlow()

    private val _shoutoutCount = MutableStateFlow(0)
    val shoutoutCount: StateFlow<Int> = _shoutoutCount.asStateFlow()

    init {
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(null)
                    else repository.getUserById(id)
                }
                .catch { emit(null) }
                .collect { u -> _user.value = u }
        }
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(emptyList())
                    else repository.getBookingsForUser(id)
                }
                .catch { emit(emptyList()) }
                .collect { b -> _bookings.value = b }
        }
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(0)
                    else repository.getReviewCountForUser(id)
                }
                .catch { emit(0) }
                .collect { _reviewCount.value = it }
        }
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(0)
                    else repository.getCommentCountForUser(id)
                }
                .catch { emit(0) }
                .collect { _shoutoutCount.value = it }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userSession.clear()
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            val user = _user.value ?: return@launch
            val path = withContext(Dispatchers.IO) {
                AvatarPersistence.copyAvatarToInternalStorage(appContext, user.id, uri)
            } ?: return@launch
            repository.updateUser(user.copy(imageUrl = path))
        }
    }
}
