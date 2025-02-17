package com.ohanyan.mathgame.playground.count

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun LearnCountScreen(
    viewModel: LearnCountViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LearnCountScreenUI(
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@Composable
fun LearnCountScreenUI(
    uiState: LearnCountUIState,
    onBackClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.darkPurpleGray90.copy(0.4f),
                        MathAppTheme.colors.darkPurpleGray90.copy(0.1f)
                    )
                )
            ),
    ) {
        ActionButton(
            modifier = Modifier.align(Alignment.TopStart),
            visible = true,
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

    }
}

@Preview
@Composable
fun LearnCountScreenPreview() {
    LearnCountScreenUI(LearnCountUIState())
}
