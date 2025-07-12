package com.ohanyan.mathgame.playground.writing

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
fun LearnNumbersScreen(
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
        }
    )
}

@Composable
fun LearnNumbersScreenUI(
    uiState: LearnNumbersUIState,
    onNumberWritten: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onDragEnd: (strokes: List<Ink.Stroke>) -> Unit = {},
) {
    var textSize by remember { mutableStateOf(IntSize.Zero) } // Store size (width, height)
    val boardText = uiState.boardText
    var boardTextOffset by remember { mutableStateOf(Offset.Zero) }

    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (uiState.isAnswerCorrect == DrawState.CORRECT) IntOffset(60, -85) else IntOffset(0, 0),
        animationSpec = tween(durationMillis = 2000),
        finishedListener = {
            if (uiState.isAnswerCorrect == DrawState.CORRECT) onNumberWritten()
        },
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

        ChalkBoard(
            title = "draw yourself"
        ) {
            SuccessLottie(
                isVisible = uiState.isAnswerCorrect == DrawState.CORRECT,
                modifier = Modifier.fillMaxSize()
            )

            if (uiState.isAnswerCorrect == DrawState.CORRECT) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 64.dp)
                        .offset(x = animatedOffset.x.dp, y = animatedOffset.y.dp),
                    text = uiState.currentNumber.toString(),
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.secondaryWhite,
                    fontSize = 32.sp,
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 64.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .onGloballyPositioned { layoutCoordinates ->
                            boardTextOffset = layoutCoordinates.positionInParent()
                            textSize = layoutCoordinates.size
                        },
                    text = boardText,
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.secondaryWhite,
                    fontSize = 32.sp,
                )

                DrawOnCanvas(
                    number = uiState.currentNumber,
                    modifier = Modifier
                        .padding(vertical = 32.dp, horizontal = 124.dp)
                        .border(
                            width = 2.dp,
                            color = MathAppTheme.colors.secondaryWhite,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    onDragEnd = onDragEnd
                )

            }
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
fun LearnNumbersScreenUIPreview(){
    LearnNumbersScreenUI(LearnNumbersUIState())
}