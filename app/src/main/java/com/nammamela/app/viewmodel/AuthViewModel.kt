package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.User
import com.nammamela.app.domain.model.UserRole
import com.nammamela.app.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Email and Password required") }
            return
        }

        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null) {
                if (user.password == pass) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _loginSuccess.emit(true)
                } else {
                    _errorEvent.emit("Incorrect password!")
                }
            } else {
                _errorEvent.emit("Account not found. Please register first.")
            }
        }
    }

    fun signUp(
        name: String, 
        email: String, 
        pass: String, 
        role: UserRole = UserRole.USER,
        companyName: String? = null,
        location: String? = null,
        autoLogin: Boolean = true
    ) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Required fields are empty") }
            return
        }

        viewModelScope.launch {
            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                _errorEvent.emit("Email already registered!")
                return@launch
            }

            val newUser = User(
                name = name,
                email = email,
                password = pass,
                role = role,
                companyName = companyName,
                location = location,
                handle = "@${name.replace(" ", "").lowercase()}"
            )
            repository.insertUser(newUser)
            
            if (autoLogin) {
                _currentUser.value = newUser
                _isLoggedIn.value = true
            }
            _loginSuccess.emit(true)
        }
    }

    fun adminLogin(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            viewModelScope.launch { _errorEvent.emit("Manager email and password required") }
            return
        }

        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null) {
                if (user.role == UserRole.ADMIN) {
                    if (user.password == pass) {
                        _currentUser.value = user
                        _isLoggedIn.value = true
                        _loginSuccess.emit(true)
                    } else {
                        _errorEvent.emit("Incorrect Manager Password!")
                    }
                } else {
                    _errorEvent.emit("This email is not registered as a Manager.")
                }
            } else {
                _errorEvent.emit("Manager account not found.")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }
}
