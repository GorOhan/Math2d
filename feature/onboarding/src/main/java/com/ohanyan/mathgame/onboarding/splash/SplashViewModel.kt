package com.ohanyan.mathgame.onboarding.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(title = "Math Game") }
    }
}

data class SplashUIState(
    val title: String = ""
)