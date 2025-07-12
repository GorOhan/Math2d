package com.ohanyan.mathgame.settings.settings

import androidx.lifecycle.ViewModel
import com.ohanyan.common.musicmanager.MusicManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {

    init {
        MusicManager.stop()
    }

}