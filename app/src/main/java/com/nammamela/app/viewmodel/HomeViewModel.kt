package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    init {
        loadPlays()
    }

    private fun loadPlays() {
        viewModelScope.launch {
            repository.getAllPlays()
                .map { list ->
                    // Sort by timestamp and take the 3 most recent for the slider
                    list.sortedByDescending { it.timestamp }.take(3)
                }
                .catch { 
                    // Prevent crash on DB errors, fallback to empty/mock
                    emit(emptyList()) 
                }
                .collect { playList ->
                    if (playList.isEmpty()) {
                        _plays.value = listOf(
                            Play(id = 1, title = "SATI-SAVITRI", duration = "180 mins", description = "A legendary tale of devotion and courage.", genre = "Mythology", posterUrl = null, rating = 4.9f),
                            Play(id = 2, title = "BHAKTA PRAHLADA", duration = "165 mins", description = "The story of the young devotee and Lord Narasimha.", genre = "Classic", posterUrl = null, rating = 4.8f),
                            Play(id = 3, title = "MALAYALA MANTRI", duration = "150 mins", description = "A witty political drama with sharp dialogue.", genre = "Comedy", posterUrl = null, rating = 4.7f)
                        )
                    } else {
                        _plays.value = playList
                    }
                }
        }
    }
}
