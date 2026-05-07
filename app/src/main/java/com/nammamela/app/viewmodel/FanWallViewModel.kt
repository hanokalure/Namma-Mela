package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.model.Comment
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

    private val _currentUser = MutableStateFlow<com.nammamela.app.domain.model.User?>(null)

    private val _errorEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _onlineCount = MutableStateFlow("12.4k")
    val onlineCount: StateFlow<String> = _onlineCount.asStateFlow()

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
        viewModelScope.launch {
            val random = java.util.Random()
            while (true) {
                val base = 15800
                val fluctuation = random.nextInt(600) - 300 // +/- 300
                val total = base + fluctuation
                _onlineCount.value = String.format("%.1fk", total / 1000.0)
                delay(2000)
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
        val user = _currentUser.value
        if (user == null) {
            viewModelScope.launch {
                _errorEvent.emit("Sign in from the login screen, then open Fan Wall from the bottom navigation.")
            }
            return
        }
        viewModelScope.launch {
            try {
                repository.insertComment(
                    Comment(
                        userId = user.id,
                        username = user.name,
                        userHandle = user.handle.ifBlank { "@${user.email.substringBefore("@")}" },
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
