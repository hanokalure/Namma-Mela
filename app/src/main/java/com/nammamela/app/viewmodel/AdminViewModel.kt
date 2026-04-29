package com.nammamela.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun saveTonightPlay(
        title: String,
        description: String,
        genre: String,
        duration: String,
        price: String
    ) {
        viewModelScope.launch {
            // 1. Archive all old plays
            repository.archiveAllPlays()
            
            // 2. Create new active play
            val play = Play(
                title = title,
                duration = duration,
                description = description,
                genre = genre,
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
        }
    }

    fun resetSuccessState() {
        _isSaveSuccess.value = false
    }
}
