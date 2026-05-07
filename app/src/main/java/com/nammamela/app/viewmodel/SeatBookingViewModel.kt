package com.nammamela.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.Booking
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.model.SeatStatus
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
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
    private val userSession: UserSession,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playId: Int = savedStateHandle.get<Int>("playId") ?: 0

    private val _play = MutableStateFlow<Play?>(null)
    val play: StateFlow<Play?> = _play.asStateFlow()

    private val _availableShowTimes = MutableStateFlow<List<String>>(emptyList())
    val availableShowTimes: StateFlow<List<String>> = _availableShowTimes.asStateFlow()

    private val _selectedShowTime = MutableStateFlow<String?>(null)
    val selectedShowTime: StateFlow<String?> = _selectedShowTime.asStateFlow()

    private val _ticketPricePerSeat = MutableStateFlow(150.0)
    val ticketPricePerSeat: StateFlow<Double> = _ticketPricePerSeat.asStateFlow()

    private val _seats = MutableStateFlow<List<Seat>>(emptyList())
    val seats: StateFlow<List<Seat>> = _seats.asStateFlow()

    private val _selectedSeats = MutableStateFlow<List<Seat>>(emptyList())
    val selectedSeats: StateFlow<List<Seat>> = _selectedSeats.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var mergedSeatJob: Job? = null
    private var seatLayoutJob: Job? = null

    init {
        loadPlayDetails()
        observeMergedSeats()
    }

    private fun loadPlayDetails() {
        if (playId <= 0) return
        viewModelScope.launch {
            repository.getPlayById(playId).collect { p ->
                _play.value = p
                val slots = parseShowTimes(p?.showTime)
                
                // Keep all shows for the current day to satisfy "keep it only for today date"
                // The manager will archive the play when it's truly over.
                val filteredSlots = slots
                _availableShowTimes.value = filteredSlots
                val price = p?.ticketPrice?.takeIf { it > 0 } ?: 150.0
                _ticketPricePerSeat.value = price
                val current = _selectedShowTime.value
                if (filteredSlots.isEmpty()) {
                    _selectedShowTime.value = null
                } else if (current == null || current !in filteredSlots) {
                    _selectedShowTime.value = filteredSlots.first()
                }
            }
        }
    }

    fun selectShowTime(time: String) {
        _selectedShowTime.value = time
        _selectedSeats.value = emptyList()
    }

    private fun observeMergedSeats() {
        if (playId <= 0) return
        if (mergedSeatJob?.isActive == true) return
        mergedSeatJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                combine(
                    repository.getSeatsForPlay(playId),
                    repository.getBookingsForPlay(playId),
                    _selectedShowTime,
                    _availableShowTimes,
                    _selectedSeats
                ) { seatList, bookings, slot, slots, selectedSeatObjs ->
                    if (seatList.size != 66) {
                        if (seatLayoutJob?.isActive != true) {
                            seatLayoutJob = viewModelScope.launch {
                                try {
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
                        emptyList()
                    } else {
                        mergeSeatDisplay(seatList, bookings, slot, slots, selectedSeatObjs)
                    }
                }.collect { merged ->
                    if (merged.size == 66) {
                        _seats.value = merged.sortedWith(compareBy({ it.row }, { it.column }))
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to load seats: ${e.localizedMessage}"))
            }
        }
    }

    fun loadSeats() {
        observeMergedSeats()
    }

    private fun seatKey(seat: Seat) = "${seat.row}${seat.column}"

    private fun parseSeatCodes(csv: String): Set<String> =
        csv.split(',').map { it.trim().uppercase().replace("\\s".toRegex(), "") }.filter { it.isNotEmpty() }.toSet()

    private fun normalizeSlot(s: String) =
        s.trim().replace("\\s+".toRegex(), " ").lowercase()

    private fun bookingMatchesSlot(booking: Booking, slot: String?, slots: List<String>): Boolean {
        if (slot == null) return false
        val ns = normalizeSlot(slot)
        val bt = booking.showTime.trim()
        if (bt.isNotEmpty()) return normalizeSlot(bt) == ns
        if (slots.isEmpty()) return true
        if (slots.size <= 1) return normalizeSlot(slots.first()) == ns
        return true
    }

    /**
     * Booked for this show slot comes from [Booking] rows. Seats marked BOOKED in DB without any
     * booking are treated as admin holds (block every slot). Fan confirmations no longer write Seat rows.
     */
    private fun mergeSeatDisplay(
        physical: List<Seat>,
        bookings: List<Booking>,
        slot: String?,
        slots: List<String>,
        selectedSeatObjs: List<Seat>
    ): List<Seat> {
        val selectedIds = selectedSeatObjs.map { it.id }.toSet()
        val bookedKeysForSlot = bookings
            .filter { bookingMatchesSlot(it, slot, slots) }
            .flatMap { parseSeatCodes(it.seats) }
            .toSet()
        return physical.map { seat ->
            val key = seatKey(seat)
            val fromBooking = key in bookedKeysForSlot
            val inAnyBooking = bookings.any { parseSeatCodes(it.seats).contains(key) }
            val adminHold = seat.status == SeatStatus.BOOKED && !inAnyBooking
            val isBooked = fromBooking || adminHold
            when {
                isBooked -> seat.copy(status = SeatStatus.BOOKED)
                seat.id in selectedIds -> seat.copy(status = SeatStatus.SELECTED)
                else -> seat.copy(status = SeatStatus.AVAILABLE)
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

        val currentSelected = _selectedSeats.value.toMutableList()
        val isAlreadySelected = currentSelected.any { it.id == seat.id }
        
        if (!isAlreadySelected) {
            if (currentSelected.size >= 10) {
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackbar("You can only book up to 10 seats at once."))
                }
                return
            }
            currentSelected.add(seat)
        } else {
            currentSelected.removeAll { it.id == seat.id }
        }
        _selectedSeats.value = currentSelected
    }

    fun confirmReservation() {
        viewModelScope.launch {
            val userId = userSession.userId.value
            if (userId == null) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please sign in again to complete your booking."))
                return@launch
            }
            val slots = _availableShowTimes.value
            if (slots.isEmpty()) {
                _uiEvent.emit(UiEvent.ShowSnackbar("No show times are available for this play. Ask the manager to add show times."))
                return@launch
            }
            val chosen = _selectedShowTime.value
            if (chosen == null || chosen !in slots) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please select a show time."))
                return@launch
            }
            if (_selectedSeats.value.isEmpty()) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please select at least one seat."))
                return@launch
            }

            _isLoading.value = true
            try {
                val seatsToBook = _selectedSeats.value

                val unit = _ticketPricePerSeat.value
                val booking = Booking(
                    userId = userId,
                    playId = playId,
                    seats = seatsToBook.joinToString(",") { "${it.row}${it.column}" },
                    totalPrice = seatsToBook.size * unit,
                    showTime = chosen,
                    timestamp = System.currentTimeMillis()
                )
                val bookingId = repository.insertBooking(booking)

                repository.insertNotification(
                    com.nammamela.app.domain.model.Notification(
                        userId = userId,
                        title = "Booking Confirmed!",
                        message = "Your seats (${booking.seats}) for $chosen are reserved. Enjoy the show!",
                        type = "BOOKING"
                    )
                )

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

    companion object {
        fun parseShowTimes(raw: String?): List<String> =
            raw?.split(',')
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()
    }
}
