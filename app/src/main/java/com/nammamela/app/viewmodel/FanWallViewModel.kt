package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Comment
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
class FanWallViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _currentUser = MutableStateFlow<com.nammamela.app.domain.model.User?>(null)

    private val _errorEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _onlineCount = MutableStateFlow("12.4k")
    val onlineCount: StateFlow<String> = _onlineCount.asStateFlow()

    init {
        // Fetch current user (Mocked to ID 1 for this flow)
        viewModelScope.launch {
            repository.getUserById(1).collect { _currentUser.value = it }
        }
        viewModelScope.launch {
            repository.getAllComments()
                .catch { emit(emptyList()) }
                .collect { list ->
                    _comments.value = list
                }
        }
    }

    fun onInputChanged(text: String) { _inputText.value = text }

    fun postComment() {
        val text = _inputText.value.trim()
        if (text.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Cannot post an empty shoutout!") }
            return
        }
        viewModelScope.launch {
            try {
                val user = _currentUser.value
                repository.insertComment(
                    Comment(
                        userId = user?.id ?: 1,
                        username = user?.name ?: "Guest Fan",
                        userHandle = user?.email?.let { "@${it.split("@")[0]}" } ?: "@guest",
                        content = text,
                        timestamp = System.currentTimeMillis(),
                        likes = 0,
                        fires = 0,
                        replies = 0
                    )
                )
                _inputText.value = ""
            } catch (e: Exception) {
                _errorEvent.emit("Failed to post: ${e.localizedMessage}")
            }
        }
    }

    fun likeComment(comment: Comment) {
        viewModelScope.launch {
            repository.updateComment(comment.copy(likes = comment.likes + 1))
        }
    }

    fun fireComment(comment: Comment) {
        viewModelScope.launch {
            repository.updateComment(comment.copy(fires = comment.fires + 1))
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch { repository.deleteComment(comment) }
    }
}
