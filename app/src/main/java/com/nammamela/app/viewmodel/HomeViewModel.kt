package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Category
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _fanFavorites = MutableStateFlow<List<com.nammamela.app.domain.model.Actor>>(emptyList())
    val fanFavorites: StateFlow<List<com.nammamela.app.domain.model.Actor>> = _fanFavorites.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    val filteredPlays: StateFlow<List<Play>> = combine(
        _plays,
        _selectedCategoryId,
        _categories
    ) { plays, catId, cats ->
        if (catId == null) plays
        else {
            val cat = cats.find { it.id == catId } ?: return@combine plays
            plays.filter { play ->
                play.genre.contains(cat.name, ignoreCase = true) ||
                    cat.name.contains(play.genre, ignoreCase = true) ||
                    play.title.contains(cat.name, ignoreCase = true) ||
                    play.description.contains(cat.name, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            repository.getAllPlays()
                .map { list -> list.filter { it.isActive } }
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
                .map { it.take(5) }
                .collect { _fanFavorites.value = it }
        }
    }

    fun onCategoryChipClicked(category: Category) {
        _selectedCategoryId.update { current ->
            if (current == category.id) null else category.id
        }
    }
}
