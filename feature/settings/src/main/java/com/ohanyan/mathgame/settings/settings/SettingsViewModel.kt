package com.ohanyan.mathgame.settings.settings

import androidx.lifecycle.ViewModel
import com.ohanyan.common.languagemanager.LocaleHelper
import com.ohanyan.common.musicmanager.MusicManager
import com.ohanyan.mathgame.settings.settings.model.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingScreenUIState(isMusicPlaying = MusicManager.isPlaying))
    val uiState = _uiState.asStateFlow()


    fun onMusicOnChange(isChecked: Boolean) {
        _uiState.update {
            it.copy(isMusicPlaying = isChecked)
        }

        MusicManager.checkPlayingState(isChecked)
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