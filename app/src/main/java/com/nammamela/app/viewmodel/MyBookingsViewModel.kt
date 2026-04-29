package com.nammamela.app.viewmodel

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
class MyBookingsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<BookingWithPlay>>(emptyList())
    val bookings: StateFlow<List<BookingWithPlay>> = _bookings.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            // Fetch bookings with play details for user 1
            repository.getBookingsWithPlayForUser(1)
                .catch { emit(emptyList()) }
                .collect { bookingList ->
                    _bookings.value = bookingList
                }
        }
    }
}
