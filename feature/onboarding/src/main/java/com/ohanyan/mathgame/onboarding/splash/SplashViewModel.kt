package com.ohanyan.mathgame.onboarding.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ohanyan.mathgame.common.data.UserPreferencesRepository
import com.ohanyan.mathgame.onboarding.navigation.OnBoardingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(title = "Math Game") }


        userPreferencesRepository.selectedAge.onEach { age ->
            if (age > 0) {
                _uiState.update { it.copy(nextScreen = OnBoardingScreen.MenuScreen) }
            }
        }.launchIn(viewModelScope)
    }
}

data class SplashUIState(
    val title: String = "",
    val nextScreen: OnBoardingScreen = OnBoardingScreen.SelectAgeScreen,
)