package com.ohanyan.mathgame.onboarding.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ohanyan.mathgame.common.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(title = "Math Game") }

        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { isOnline ->
                _uiState.update { it.copy(isOnline = isOnline) }
            }
        }
    }
}

data class MenuUIState(
    val title: String = "",
    val isOnline: Boolean = true
)