package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Notification
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _errorEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotifications()
                .catch { emit(emptyList()) }
                .collect { list ->
                    _notifications.value = list
                }
        }
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            try {
                repository.markNotificationAsRead(id)
            } catch (e: Exception) {
                _errorEvent.emit("Failed to mark as read: ${e.localizedMessage}")
            }
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(notification)
            } catch (e: Exception) {
                _errorEvent.emit("Failed to delete alert: ${e.localizedMessage}")
            }
        }
    }
}
