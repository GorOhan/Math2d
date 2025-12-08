package com.ohanyan.mathgame.playground.writing

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.vision.digitalink.Ink
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType
import com.ohanyan.ui.component.success.SuccessLottie

@Composable
internal fun LearnNumbersScreen(
    viewModel: LearnNumbersViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LearnNumbersScreenUI(
        uiState = uiState,
        onNumberWritten = {
            viewModel.makeBoardText(BoardTextState.FULL)
        },
        onBackClick = onBackClick,
        onDragEnd = {
            viewModel.recognize(it)
        },
        onTryAgain = {
            viewModel.resetError()
        }
    )
}

@Composable
private fun LearnNumbersScreenUI(
    uiState: LearnNumbersUIState,
    onNumberWritten: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onDragEnd: (strokes: List<Ink.Stroke>) -> Unit = {},
    onTryAgain: () -> Unit = {}
) {
    val boardText = uiState.boardText

    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (uiState.isAnswerCorrect == DrawState.CORRECT) IntOffset(
            60,
            -85
        ) else IntOffset(0, 0),
        animationSpec = tween(durationMillis = 2000),
        finishedListener = {
            if (uiState.isAnswerCorrect == DrawState.CORRECT) onNumberWritten()
        },
        label = ""
    )

    // Shake animation for board text when there's an error
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.coreBlue.copy(0.4f),
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

        ChalkBoard {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 64.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer(
                                translationX = if (uiState.isAnswerCorrect == DrawState.WRONG) shakeOffset else 0f
                            ),
                        text = boardText,
                        style = MathAppTheme.typography.chalk,
                        color = MathAppTheme.colors.secondaryWhite,
                        textAlign = TextAlign.Center,
                        fontSize = 184.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)

                ) {
                    HintDigitAnimation(
                        number = uiState.currentNumber,
                        modifier = Modifier,
                        boardText = uiState.boardText,
                        resetCanvas = uiState.isAnswerCorrect == DrawState.WRONG
                    )
                }

                DrawOnCanvas(
                    number = uiState.currentNumber,
                    modifier = Modifier
                        .weight(1f),
                    onDragEnd = onDragEnd,
                    resetCanvas = uiState.isAnswerCorrect == DrawState.WRONG
                )
            }

            SuccessLottie(
                isVisible = uiState.isAnswerCorrect == DrawState.CORRECT,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainHero(
                modifier = Modifier
                    .padding(16.dp)
                    .size(112.dp)
            )
        }
    }
}

@Composable
@MathPreview
fun LearnNumbersScreenUIPreview() {
    LearnNumbersScreenUI(LearnNumbersUIState())
}