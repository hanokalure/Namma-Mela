package com.nammamela.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.BookingWithPlay
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketConfirmationViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingId: Int = savedStateHandle.get<Int>("bookingId") ?: 0

    private val _booking = MutableStateFlow<BookingWithPlay?>(null)
    val booking: StateFlow<BookingWithPlay?> = _booking.asStateFlow()

    init {
        loadBooking()
    }

    private fun loadBooking() {
        if (bookingId == 0) return
        viewModelScope.launch {
            repository.getBookingWithPlayById(bookingId)
                .catch { emit(null) }
                .collect { b ->
                    _booking.value = b
                }
        }
    }
}
