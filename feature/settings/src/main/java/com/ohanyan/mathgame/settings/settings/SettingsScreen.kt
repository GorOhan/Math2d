package com.ohanyan.mathgame.settings.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
   Box {}
}