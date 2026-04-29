package com.nammamela.app.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCastViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playId: Int = savedStateHandle.get<Int>("playId") ?: 1

    private val _cast = MutableStateFlow<List<Actor>>(emptyList())
    val cast: StateFlow<List<Actor>> = _cast.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    init {
        loadCast()
    }

    private fun loadCast() {
        viewModelScope.launch {
            repository.getActorsForPlay(playId).collect { castList ->
                _cast.value = castList
            }
        }
    }

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun addActor(name: String, role: String, category: String) {
        viewModelScope.launch {
            val actor = Actor(
                playId = playId,
                name = name,
                role = role,
                category = category,
                imageUrl = _selectedImageUri.value?.toString()
            )
            repository.insertActor(actor)
            _selectedImageUri.value = null // Reset for next add
        }
    }

    fun deleteActor(actor: Actor) {
        viewModelScope.launch {
            repository.deleteActor(actor)
        }
    }
}
