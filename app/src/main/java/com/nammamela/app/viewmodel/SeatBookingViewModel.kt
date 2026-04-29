package com.nammamela.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.model.SeatStatus
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class BookingSuccess(val bookingId: Int) : UiEvent()
}

@HiltViewModel
class SeatBookingViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playId: Int = savedStateHandle.get<Int>("playId") ?: 1

    private val _seats = MutableStateFlow<List<Seat>>(emptyList())
    val seats: StateFlow<List<Seat>> = _seats.asStateFlow()

    private val _selectedSeats = MutableStateFlow<List<Seat>>(emptyList())
    val selectedSeats: StateFlow<List<Seat>> = _selectedSeats.asStateFlow()
    
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadSeats()
    }

    fun loadSeats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getSeatsForPlay(playId).collect { seatList ->
                    // Replicated 11-seat layout check
                    if (seatList.size != 66) {
                        initializeSeats()
                    } else {
                        _seats.value = seatList.sortedWith(compareBy({ it.row }, { it.column }))
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to load seats: ${e.localizedMessage}"))
            }
        }
    }

    private fun initializeSeats() {
        viewModelScope.launch {
            try {
                // DELETE OLD DATA TO PREVENT DUPLICATES
                repository.deleteSeatsForPlay(playId)
                
                val initialSeats = mutableListOf<Seat>()
                val rows = listOf("A", "B", "C", "D", "E", "F")
                for (row in rows) {
                    for (col in 1..11) {
                        initialSeats.add(Seat(playId = playId, row = row, column = col))
                    }
                }
                repository.insertSeats(initialSeats)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to initialize theater: ${e.localizedMessage}"))
            }
        }
    }

    fun toggleSeatSelection(seat: Seat) {
        if (seat.status == SeatStatus.BOOKED) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("This seat is already booked!"))
            }
            return
        }

        _seats.update { currentSeats ->
            currentSeats.map { s ->
                if (s.id == seat.id) {
                    val newStatus = if (s.status == SeatStatus.AVAILABLE) SeatStatus.SELECTED else SeatStatus.AVAILABLE
                    val updatedSeat = s.copy(status = newStatus)
                    
                    val currentSelected = _selectedSeats.value.toMutableList()
                    if (newStatus == SeatStatus.SELECTED) {
                        if (currentSelected.size >= 10) {
                            viewModelScope.launch {
                                _uiEvent.emit(UiEvent.ShowSnackbar("You can only book up to 10 seats at once."))
                            }
                            return@map s
                        }
                        currentSelected.add(updatedSeat)
                    } else {
                        currentSelected.removeAll { it.id == updatedSeat.id }
                    }
                    _selectedSeats.value = currentSelected
                    
                    updatedSeat
                } else s
            }
        }
    }

    fun confirmReservation(userId: Int) {
        viewModelScope.launch {
            if (_selectedSeats.value.isEmpty()) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please select at least one seat."))
                return@launch
            }
            
            _isLoading.value = true
            try {
                val seatsToBook = _selectedSeats.value.map { it.copy(status = SeatStatus.BOOKED) }
                repository.updateSeats(seatsToBook)
                
                val booking = com.nammamela.app.domain.model.Booking(
                    userId = userId,
                    playId = playId,
                    seats = seatsToBook.joinToString(",") { "${it.row}${it.column}" },
                    totalPrice = seatsToBook.size * 150.0,
                    timestamp = System.currentTimeMillis()
                )
                val bookingId = repository.insertBooking(booking)
                
                _selectedSeats.value = emptyList()
                _isLoading.value = false
                _uiEvent.emit(UiEvent.BookingSuccess(bookingId.toInt()))
            } catch (e: Exception) {
                _isLoading.value = false
                _uiEvent.emit(UiEvent.ShowSnackbar("Booking failed: ${e.localizedMessage}"))
            }
        }
    }
    
    fun toggleSeatStatus(seat: Seat) {
        viewModelScope.launch {
            try {
                val newStatus = if (seat.status == SeatStatus.AVAILABLE) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                repository.updateSeats(listOf(seat.copy(status = newStatus)))
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Update failed: ${e.localizedMessage}"))
            }
        }
    }
}
