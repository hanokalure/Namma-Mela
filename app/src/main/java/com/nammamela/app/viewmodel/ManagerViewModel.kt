package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

const val VENUE_SEAT_CAPACITY = 66

data class PlayInsight(
    val play: Play,
    val bookingCount: Int,
    val revenue: Double,
    val ticketsSold: Int
) {
    val utilization: Float
        get() = (ticketsSold.toFloat() / VENUE_SEAT_CAPACITY).coerceIn(0f, 1f)
}

@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _totalBookings = MutableStateFlow(0)
    val totalBookings: StateFlow<Int> = _totalBookings.asStateFlow()

    private val _totalRevenue = MutableStateFlow(0)
    val totalRevenue: StateFlow<Int> = _totalRevenue.asStateFlow()

    private val _insights = MutableStateFlow<List<PlayInsight>>(emptyList())
    val insights: StateFlow<List<PlayInsight>> = _insights.asStateFlow()

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllPlays(),
                repository.getAllBookings()
            ) { plays, bookings ->
                _plays.value = plays.sortedByDescending { it.timestamp }
                _totalBookings.value = bookings.size
                _totalRevenue.value = bookings.sumOf { it.totalPrice }.toInt()

                _insights.value = plays.map { play ->
                    val forPlay = bookings.filter { it.playId == play.id }
                    val revenue = forPlay.sumOf { it.totalPrice }
                    val ticketsSold = forPlay.sumOf { b ->
                        b.seats.split(",").count { part -> part.isNotBlank() }
                    }
                    PlayInsight(
                        play = play,
                        bookingCount = forPlay.size,
                        revenue = revenue,
                        ticketsSold = ticketsSold
                    )
                }.sortedByDescending { it.play.timestamp }
            }.collect { }
        }
    }
}
