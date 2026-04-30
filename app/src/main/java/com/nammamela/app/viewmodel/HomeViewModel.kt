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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    private val _categories = MutableStateFlow<List<com.nammamela.app.domain.model.Category>>(emptyList())
    val categories: StateFlow<List<com.nammamela.app.domain.model.Category>> = _categories.asStateFlow()

    private val _fanFavorites = MutableStateFlow<List<com.nammamela.app.domain.model.Actor>>(emptyList())
    val fanFavorites: StateFlow<List<com.nammamela.app.domain.model.Actor>> = _fanFavorites.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAllPlays()
                .map { list ->
                    list.sortedByDescending { it.timestamp }.take(3)
                }
                .catch { 
                    _errorEvent.emit("Failed to load plays")
                    emit(emptyList()) 
                }
                .collect { playList ->
                    _plays.value = playList
                }
        }

        viewModelScope.launch {
            repository.getAllCategories().collect { _categories.value = it }
        }

        viewModelScope.launch {
            repository.getAllActors()
                .map { it.take(5) } // Show first 5 as favorites
                .collect { _fanFavorites.value = it }
        }
    }
}
