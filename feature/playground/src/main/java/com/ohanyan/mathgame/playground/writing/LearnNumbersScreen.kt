package com.ohanyan.mathgame.playground.writing

import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.NextNumber
import com.ohanyan.ui.component.PlayGame
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mainhero.DogAnimate
import com.ohanyan.ui.component.mainhero.EraseEffect
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType
import com.ohanyan.ui.component.numberhint.NumberHint
import com.ohanyan.ui.component.success.SuccessLottie
import com.ohanyan.ui.component.undo.UndoButton

@Composable
internal fun LearnNumbersScreen(
    viewModel: LearnNumbersViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tickerState = viewModel.tickerState.collectAsStateWithLifecycle()
    val path = viewModel.path.collectAsStateWithLifecycle()

    LearnNumbersScreenUI(
        uiState = uiState,
        tickerState = tickerState,
        onBackClick = onBackClick,
        path = path,
        onStartGame = viewModel::learnNextNumber,
        afterDraw = viewModel::afterDraw,
        onUndo = viewModel::undoDrawing,
        addPoint = viewModel::addPoints
    )
}

@Composable
private fun LearnNumbersScreenUI(
    uiState: LearnNumbersUIState,
    tickerState: State<TickerState>,
    path: State<Path>,
    onStartGame: () -> Unit = {},
    afterDraw: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onUndo: () -> Unit = {},
    addPoint: (offsetX: Float, offsetY: Float, pathAction: PathAction) -> Unit = { _, _, _ -> }
) {
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
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

        ChalkBoard {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.playState != PlayState.START
                        && uiState.playState != PlayState.NONE
                    ) {
                        Box {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(32.dp),
                                text = uiState.boardText,
                                style = MathAppTheme.typography.chalk,
                                color = MathAppTheme.colors.secondaryWhite,
                                textAlign = TextAlign.Center,
                                fontSize = 64.sp,
                            )
                        }
                    }
                    when (uiState.playState) {
                        PlayState.START -> {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth(),
                                    text = uiState.boardText,
                                    style = MathAppTheme.typography.chalk,
                                    color = MathAppTheme.colors.secondaryWhite,
                                    textAlign = TextAlign.Center,
                                    fontSize = 224.sp,
                                )
                            }
                        }

                        PlayState.HINT -> {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(0.3f)
                                    .fillMaxHeight()

                            ) {
                                NumberHint(
                                    number = uiState.currentNumber
                                )
                            }
                        }

                        PlayState.DRAW -> {
                            DrawingArea(
                                number = uiState.currentNumber,
                                modifier = Modifier,
                                path = path,
                                shouldShake = uiState.showErrorLottie,
                                addPoint = addPoint,
                            )

                            if (uiState.showHintChalk) {
                                HintChalk(
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )
                            }

                            var showEraser by remember { mutableStateOf(false) }
                            if (showEraser) {
                                EraseEffect(
                                    modifier = Modifier.fillMaxSize(),
                                    onUndo = { onUndo() },
                                    onFinish = { showEraser = false }
                                )
                            }

                            if (!showEraser) {
                                UndoButton(
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .align(Alignment.BottomStart),
                                    onClick = {
                                        showEraser = true
                                    }
                                )
                            }

                            if (!path.value.isEmpty) {
                                NextNumber(
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .align(Alignment.BottomEnd),
                                    onClick = afterDraw
                                )
                            }
                        }

                        PlayState.NONE -> {
                            PlayGame(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .align(Alignment.Center),
                                onClick = onStartGame
                            )
                        }
                    }
                }
            }
            SuccessLottie(
                isVisible = uiState.showSuccessLottie,
                modifier = Modifier.fillMaxSize()
            )

            DogAnimate(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                isVisible = uiState.showErrorLottie
            )
        }

        TimeTicker(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(32.dp),
            state = tickerState
        )

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
    //LearnNumbersScreenUI(LearnNumbersUIState())
}
