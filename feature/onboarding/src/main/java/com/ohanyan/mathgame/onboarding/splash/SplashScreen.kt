package com.ohanyan.mathgame.onboarding.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.component.header.HeaderFromTop
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SplashScreenUI(uiState = uiState)
}

@Composable
fun SplashScreenUI(uiState: SplashUIState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MathAppTheme.colors.coreBlue)
    ) {
        HeaderFromTop(uiState.title)
    }
}

@Preview
@Composable
fun SplashPreview() {
    SplashScreen()
}