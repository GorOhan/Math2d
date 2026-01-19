package com.ohanyan.mathgame.settings.settings

import androidx.lifecycle.viewModelScope
import com.ohanyan.mathgame.common.data.UserPreferencesRepository
import com.ohanyan.mathgame.settings.settings.model.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.musicOn.collect { isMusicOn ->
                _uiState.update { state ->
                    state.copy(isMusicPlaying = isMusicOn)
                }
            }
        }
    }

    fun onMusicOnChange(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMusicOn(isChecked)
        }
    }

    fun selectLanguage(language: AppLanguage) {
        _uiState.update {
            it.copy(selectedLanguage = language)
        }
    }


}

data class SettingScreenUIState(
    val isMusicPlaying: Boolean = false,
    val selectedLanguage: AppLanguage = AppLanguage.EN
)