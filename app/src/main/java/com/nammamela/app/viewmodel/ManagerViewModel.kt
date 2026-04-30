package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    private val _totalBookings = MutableStateFlow(0)
    val totalBookings: StateFlow<Int> = _totalBookings.asStateFlow()

    private val _totalRevenue = MutableStateFlow(0)
    val totalRevenue: StateFlow<Int> = _totalRevenue.asStateFlow()

    init {
        loadPerformanceData()
    }

    private fun loadPerformanceData() {
        viewModelScope.launch {
            repository.getAllPlays().collect { list ->
                _plays.value = list
            }
        }
        
        viewModelScope.launch {
            repository.getAllBookings().collect { bookings ->
                _totalBookings.value = bookings.size
                // In a real app, you'd sum the actual prices from the plays
                // For now, we'll use a standard price of 150 per booking
                _totalRevenue.value = bookings.size * 150
            }
        }
    }
}
