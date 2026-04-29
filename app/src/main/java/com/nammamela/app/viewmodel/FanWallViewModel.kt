package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Comment
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
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
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertComment(
                Comment(
                    userId = 1,
                    username = "Basavaraj",
                    userHandle = "@Basavaraj",
                    content = text,
                    timestamp = System.currentTimeMillis(),
                    likes = 0,
                    fires = 0,
                    replies = 0
                )
            )
            _inputText.value = ""
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
