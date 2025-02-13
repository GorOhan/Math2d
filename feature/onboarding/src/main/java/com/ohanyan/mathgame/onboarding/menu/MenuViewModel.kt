package com.ohanyan.mathgame.onboarding.menu

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(title = "Math Game") }
    }
}

data class MenuUIState(
    val title: String = ""
)