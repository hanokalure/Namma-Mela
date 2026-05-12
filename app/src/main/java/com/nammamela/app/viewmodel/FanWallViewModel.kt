package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.Comment
import com.nammamela.app.domain.model.User
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FanWallViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _pendingImageUri = MutableStateFlow<String?>(null)
    val pendingImageUri: StateFlow<String?> = _pendingImageUri.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            userSession.userId
                .flatMapLatest { id ->
                    if (id == null) flowOf(null)
                    else repository.getUserById(id)
                }
                .collect { _currentUser.value = it }
        }
        viewModelScope.launch {
            repository.getAllComments()
                .catch { emit(emptyList()) }
                .collect { list ->
                    _comments.value = list
                }
        }
    }

    fun onInputChanged(text: String) {
        _inputText.value = text
    }

    fun setPendingImageUri(uri: String?) {
        _pendingImageUri.value = uri
    }

    fun appendEmoji(emoji: String) {
        _inputText.update { it + emoji }
    }

    fun postComment() {
        val text = _inputText.value.trim()
        if (text.isBlank() && _pendingImageUri.value == null) {
            viewModelScope.launch { _errorEvent.emit("Add text or a photo before posting.") }
            return
        }
        val user = _currentUser.value
        if (user == null) {
            viewModelScope.launch {
                _errorEvent.emit("Sign in from the login screen, then open Fan Wall from the bottom navigation.")
            }
            return
        }
        viewModelScope.launch {
            try {
                val body = text.ifBlank { "📷" }
                repository.insertComment(
                    Comment(
                        userId = user.id,
                        username = user.name,
                        userHandle = user.handle.ifBlank { "@${user.email.substringBefore("@")}" },
                        content = body,
                        timestamp = System.currentTimeMillis(),
                        likes = 0,
                        fires = 0,
                        replies = 0,
                        imageUrl = _pendingImageUri.value
                    )
                )
                _inputText.value = ""
                _pendingImageUri.value = null
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
