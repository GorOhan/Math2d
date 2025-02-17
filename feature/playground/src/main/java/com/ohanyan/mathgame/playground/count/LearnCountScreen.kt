package com.ohanyan.mathgame.playground.count

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mainhero.MainHero
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

        ChalkBoard(
            modifier = Modifier.align(Alignment.Center)
        ) {

            val currentCount = uiState.options[uiState.currentOptionIndex]
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                repeat(currentCount.count) {
                    Image(
                        modifier = Modifier.size(64.dp),
                        painter = painterResource(currentCount.imgResId),
                        contentDescription = null
                    )
                }
            }
        }

        MainHero(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
                .size(112.dp)
        )
    }
}

@Preview
@Composable
fun LearnCountScreenPreview() {
    LearnCountScreenUI(LearnCountUIState())
}
