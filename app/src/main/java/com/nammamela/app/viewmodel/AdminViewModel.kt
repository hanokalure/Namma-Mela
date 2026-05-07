package com.nammamela.app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.repository.AppRepository
import com.nammamela.app.util.AvatarPersistence
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PlayCommitOutcome(val playId: Int, val openCastScreen: Boolean)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userSession: UserSession,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

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

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _commitOutcome = MutableSharedFlow<PlayCommitOutcome>(extraBufferCapacity = 1)
    val commitOutcome = _commitOutcome.asSharedFlow()

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
            combine(
                repository.getActivePlay(),
                repository.getAllBookings()
            ) { play, bookings ->
                _activePlay.value = play
                if (play == null) {
                    _bookedCount.value = 0
                    _revenue.value = 0
                } else {
                    val forPlay = bookings.filter { it.playId == play.id }
                    val ticketCount = forPlay.sumOf { b ->
                        b.seats.split(",").count { part -> part.isNotBlank() }
                    }
                    _bookedCount.value = ticketCount
                    _revenue.value = forPlay.sumOf { it.totalPrice }.toInt()
                }
            }.collect { }
        }
    }

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun clearPosterSelection() {
        _selectedImageUri.value = null
    }

    fun saveProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            val uid = userSession.userId.value ?: return@launch
            val user = repository.getUserByIdOnce(uid) ?: return@launch
            val path = withContext(Dispatchers.IO) {
                AvatarPersistence.copyAvatarToInternalStorage(appContext, uid, uri)
            } ?: return@launch
            repository.updateUser(user.copy(imageUrl = path))
        }
    }

    private fun parseTicketPrice(price: String): Double {
        return price.replace("₹", "").replace(",", "").trim().toDoubleOrNull()
            ?.takeIf { it > 0 } ?: 150.0
    }

    private fun buildSeatGrid(playId: Int): List<Seat> {
        val rows = listOf("A", "B", "C", "D", "E", "F")
        val list = mutableListOf<Seat>()
        for (row in rows) {
            for (col in 1..11) {
                list.add(Seat(playId = playId, row = row, column = col))
            }
        }
        return list
    }

    /**
     * @param editingPlayId null = create; non-null = update
     * @param setAsTonight maps to [Play.isActive] and archives other plays when true
     */
    fun commitPlay(
        editingPlayId: Int?,
        title: String,
        description: String,
        genre: String,
        duration: String,
        showTime: String,
        price: String,
        setAsTonight: Boolean,
        openCastScreenAfter: Boolean
    ) {
        if (title.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Show title is required!") }
            return
        }
        if (showTime.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Add at least one show time.") }
            return
        }

        viewModelScope.launch {
            try {
                val ticketPrice = parseTicketPrice(price)
                val posterUrl = _selectedImageUri.value?.toString()
                    ?: editingPlayId?.let { repository.getPlayByIdOnce(it)?.posterUrl }

                if (editingPlayId == null) {
                    if (setAsTonight) {
                        repository.archiveAllPlays()
                    }
                    val play = Play(
                        title = title.trim(),
                        duration = duration.trim(),
                        description = description.trim(),
                        genre = genre.trim(),
                        showTime = showTime.trim(),
                        ticketPrice = ticketPrice,
                        posterUrl = posterUrl,
                        rating = 4.9f,
                        isActive = setAsTonight
                    )
                    val newId = repository.insertPlay(play).toInt()
                    repository.insertSeats(buildSeatGrid(newId))
                    _commitOutcome.emit(PlayCommitOutcome(newId, openCastScreenAfter))
                } else {
                    val existing = repository.getPlayByIdOnce(editingPlayId)
                        ?: run {
                            _errorEvent.emit("Play not found.")
                            return@launch
                        }
                    if (setAsTonight) {
                        repository.archiveAllPlays()
                    }
                    val updated = existing.copy(
                        title = title.trim(),
                        duration = duration.trim(),
                        description = description.trim(),
                        genre = genre.trim(),
                        showTime = showTime.trim(),
                        ticketPrice = ticketPrice,
                        posterUrl = posterUrl,
                        isActive = setAsTonight
                    )
                    repository.updatePlay(updated)
                    _commitOutcome.emit(PlayCommitOutcome(editingPlayId, openCastScreenAfter))
                }
            } catch (e: Exception) {
                _errorEvent.emit("Failed to save play: ${e.localizedMessage}")
            }
        }
    }

    fun deletePlay(playId: Int) {
        viewModelScope.launch {
            try {
                repository.deletePlayCascade(playId)
            } catch (e: Exception) {
                _errorEvent.emit("Failed to delete play: ${e.localizedMessage}")
            }
        }
    }

    fun setPlayAsTonightsShow(playId: Int) {
        viewModelScope.launch {
            try {
                repository.archiveAllPlays()
                val play = repository.getPlayByIdOnce(playId) ?: return@launch
                repository.updatePlay(play.copy(isActive = true))
            } catch (e: Exception) {
                _errorEvent.emit("Failed to update active play: ${e.localizedMessage}")
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
}
