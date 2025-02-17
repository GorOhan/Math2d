package com.ohanyan.mathgame.playground.count

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LearnCountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LearnCountUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(title = "Math Game") }
    }
}

data class LearnCountUIState(
    val title: String = ""
)