package com.nammamela.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import com.nammamela.app.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userSession: UserSession,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playId: Int = savedStateHandle.get<Int>("playId") ?: 0

    private val _play = MutableStateFlow<Play?>(null)
    val play: StateFlow<Play?> = _play.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        if (playId > 0) {
            viewModelScope.launch {
                repository.getPlayById(playId)
                    .catch { emit(null) }
                    .collect { _play.value = it }
            }
        }
    }

    fun submitReview(rating: Int, text: String, onSuccess: () -> Unit) {
        if (playId <= 0) {
            viewModelScope.launch { _errorEvent.emit("Invalid show.") }
            return
        }
        viewModelScope.launch {
            val userId = userSession.userId.value
            if (userId == null) {
                _errorEvent.emit("Please sign in to submit a review.")
                return@launch
            }
            try {
                repository.submitPlayReview(playId, userId, rating, text)
                onSuccess()
            } catch (e: Exception) {
                _errorEvent.emit(e.message ?: "Could not save review.")
            }
        }
    }
}
