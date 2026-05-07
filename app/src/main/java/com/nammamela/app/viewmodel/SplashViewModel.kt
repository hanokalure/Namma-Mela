package com.nammamela.app.viewmodel

import androidx.lifecycle.ViewModel
import com.nammamela.app.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val userSession: UserSession
) : ViewModel()
