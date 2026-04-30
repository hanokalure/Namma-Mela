package com.nammamela.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.nammamela.app.domain.model.SeatStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _isSaveSuccess = MutableStateFlow(false)
    val isSaveSuccess: StateFlow<Boolean> = _isSaveSuccess.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _activePlay = MutableStateFlow<Play?>(null)
    val activePlay: StateFlow<Play?> = _activePlay.asStateFlow()

    private val _bookedCount = MutableStateFlow(0)
    val bookedCount: StateFlow<Int> = _bookedCount.asStateFlow()

    private val _revenue = MutableStateFlow(0)
    val revenue: StateFlow<Int> = _revenue.asStateFlow()

    private val _allPlays = MutableStateFlow<List<Play>>(emptyList())
    val allPlays: StateFlow<List<Play>> = _allPlays.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            repository.getAllPlays().collect { plays ->
                _allPlays.value = plays
            }
        }
        viewModelScope.launch {
            repository.getActivePlay().collect { play ->
                _activePlay.value = play
                if (play != null) {
                    // Fetch seats for this play
                    repository.getSeatsForPlay(play.id).collect { seats ->
                        val booked = seats.count { it.status == SeatStatus.BOOKED }
                        _bookedCount.value = booked
                        _revenue.value = booked * 150 // Mock price 150
                    }
                }
            }
        }
    }

    private val _errorEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun saveTonightPlay(
        title: String,
        description: String,
        genre: String,
        duration: String,
        showTime: String,
        price: String
    ) {
        if (title.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Show title is required!") }
            return
        }

        viewModelScope.launch {
            try {
                // 1. Archive all old plays
                repository.archiveAllPlays()
                
                // 2. Create new active play
                val play = Play(
                    title = title,
                    duration = duration,
                    description = description,
                    genre = genre,
                    showTime = showTime,
                    posterUrl = _selectedImageUri.value?.toString(),
                    rating = 4.9f,
                    isActive = true
                )
                val newPlayId = repository.insertPlay(play).toInt()
                
                // 3. Initialize 66 seats for this specific new show
                val initialSeats = mutableListOf<Seat>()
                val rows = listOf("A", "B", "C", "D", "E", "F")
                for (row in rows) {
                    for (col in 1..11) {
                        initialSeats.add(Seat(playId = newPlayId, row = row, column = col))
                    }
                }
                repository.insertSeats(initialSeats)
                
                _isSaveSuccess.value = true
            } catch (e: Exception) {
                _errorEvent.emit("Failed to save play: ${e.localizedMessage}")
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                repository.wipeDatabase()
                _errorEvent.emit("App Reset! All dummy data cleared.")
            } catch (e: Exception) {
                _errorEvent.emit("Reset failed: ${e.localizedMessage}")
            }
        }
    }

    fun resetSuccessState() {
        _isSaveSuccess.value = false
    }
}
