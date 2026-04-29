package com.nammamela.app.viewmodel

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

enum class CastFilter { ALL, HEROES, COMEDIANS }

@HiltViewModel
class CastViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _actors = MutableStateFlow<List<Actor>>(emptyList())
    val actors: StateFlow<List<Actor>> = _actors.asStateFlow()

    private val _filter = MutableStateFlow(CastFilter.ALL)
    val filter: StateFlow<CastFilter> = _filter.asStateFlow()

    private val _filteredActors = MutableStateFlow<List<Actor>>(emptyList())
    val filteredActors: StateFlow<List<Actor>> = _filteredActors.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllActors().collect { actorList ->
                _actors.value = actorList
                applyFilter()
            }
        }
    }

    fun setFilter(f: CastFilter) {
        _filter.value = f
        applyFilter()
    }

    private fun applyFilter() {
        _filteredActors.value = when (_filter.value) {
            CastFilter.ALL -> _actors.value
            CastFilter.HEROES -> _actors.value.filter {
                it.category.equals("Hero", ignoreCase = true) || it.category.equals("Heroine", ignoreCase = true)
            }
            CastFilter.COMEDIANS -> _actors.value.filter {
                it.category.equals("Comedian", ignoreCase = true)
            }
        }
    }
}
