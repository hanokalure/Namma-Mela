package com.nammamela.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayDetailViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playId: Int = savedStateHandle.get<Int>("playId") ?: 1

    private val _play = MutableStateFlow<Play?>(null)
    val play: StateFlow<Play?> = _play.asStateFlow()

    private val _cast = MutableStateFlow<List<Actor>>(emptyList())
    val cast: StateFlow<List<Actor>> = _cast.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _isLoading.value = true
        
        // Load Play details
        viewModelScope.launch {
            repository.getPlayById(playId)
                .catch { emit(null) }
                .collect { playData ->
                    _play.value = playData
                }
        }
        
        // Load Cast details
        viewModelScope.launch {
            repository.getActorsForPlay(playId)
                .catch { emit(emptyList()) }
                .collect { castList ->
                    _cast.value = castList
                    _isLoading.value = false
                }
        }
    }

    fun submitRating(rating: Float) {
        viewModelScope.launch {
            repository.updatePlayRating(playId, rating)
        }
    }
}
