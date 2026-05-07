package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.BookingWithPlay
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MyBookingsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<BookingWithPlay>>(emptyList())
    val bookings: StateFlow<List<BookingWithPlay>> = _bookings.asStateFlow()

    init {
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(emptyList())
                    else repository.getBookingsWithPlayForUser(id)
                }
                .catch { emit(emptyList()) }
                .collect { bookingList ->
                    _bookings.value = bookingList
                }
        }
    }
}
