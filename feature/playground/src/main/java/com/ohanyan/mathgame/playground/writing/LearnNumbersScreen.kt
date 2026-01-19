package com.ohanyan.mathgame.playground.writing

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.ui.R
import com.ohanyan.ui.component.NextNumber
import com.ohanyan.ui.component.NumberAndBus
import com.ohanyan.ui.component.PlayGame
import com.ohanyan.ui.component.Point
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mainhero.DogAnimate
import com.ohanyan.ui.component.mainhero.EraseEffect
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType
import com.ohanyan.ui.component.numberhint.NumberHint
import com.ohanyan.ui.component.soundbutton.SoundButton
import com.ohanyan.ui.component.success.SuccessLottie
import com.ohanyan.ui.component.undo.UndoButton
import kotlinx.coroutines.delay

@Composable
internal fun LearnNumbersScreen(
    viewModel: LearnNumbersViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tickerState = viewModel.tickerState.collectAsStateWithLifecycle()
    val path by viewModel.path.collectAsStateWithLifecycle()

    LearnNumbersScreenUI(
        uiState = uiState,
        tickerState = tickerState,
        onBackClick = onBackClick,
        path = path,
        onNumberChosen = viewModel::onNumberChosen,
        onStartGame = viewModel::showMenu,
        afterDraw = viewModel::afterDraw,
        onUndo = viewModel::undoDrawing,
        addPoint = viewModel::addPoints,
        onMusic = viewModel::onMusicOnChange
    )
}

@Composable
private fun LearnNumbersScreenUI(
    uiState: LearnNumbersUIState,
    tickerState: State<TickerState>,
    path: Path,
    onNumberChosen: (Int) -> Unit = {},
    onStartGame: () -> Unit = {},
    afterDraw: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onUndo: () -> Unit = {},
    onMusic: (Boolean) -> Unit = {},
    addPoint: (offsetX: Float, offsetY: Float, pathAction: PathAction) -> Unit = { _, _, _ -> }
) {
    val configuration = LocalConfiguration.current
    val isHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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

        SoundButton(
            modifier = Modifier.align(Alignment.TopEnd),
            isOn = uiState.isMusicOn,
            onClick = onMusic
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
                        && uiState.playState != PlayState.NONE &&
                        uiState.playState != PlayState.CHOOSE_NUMBER
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
                                var horizontalBiasNumber by remember { mutableFloatStateOf(-1f) }
                                LaunchedEffect(Unit) {
                                    horizontalBiasNumber = 0f
                                }
                                val animatedBiasNumber by animateFloatAsState(
                                    targetValue = horizontalBiasNumber,
                                    animationSpec = tween(2000),
                                    label = "NextButtonEntry",
                                    finishedListener = {}
                                )

                                Row(
                                    modifier = Modifier.align(
                                        BiasAlignment(
                                            animatedBiasNumber,
                                            0f
                                        )
                                    ),
                                ) {
                                    NumberAndBus()
                                    Text(
                                        text = uiState.boardText,
                                        style = MathAppTheme.typography.chalk,
                                        color = MathAppTheme.colors.secondaryWhite,
                                        textAlign = TextAlign.Center,
                                        fontSize = 148.sp,
                                    )
                                }
                            }
                        }

                        PlayState.HINT -> {
                            val fraction = if (isHorizontal) .3f else .6f
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(fraction)
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

                            if (uiState.showNextButton) {
                                var horizontalBias by remember { mutableFloatStateOf(-1f) }
                                LaunchedEffect(Unit) {
                                    horizontalBias = 1f
                                }
                                var startFlicker by remember { mutableStateOf(false) }
                                val animatedBias by animateFloatAsState(
                                    targetValue = horizontalBias,
                                    animationSpec = tween(1000),
                                    label = "NextButtonEntry",
                                    finishedListener = { startFlicker = true }
                                )

                                var isLightIcon by remember { mutableStateOf(false) }
                                LaunchedEffect(startFlicker) {
                                    if (!startFlicker) return@LaunchedEffect
                                    repeat(20) {
                                        isLightIcon = !isLightIcon
                                        delay(200)
                                        if (it == 19) startFlicker = false
                                    }
                                }

                                var isNextClicked by remember { mutableStateOf(false) }
                                val nextButtonOffset by animateDpAsState(
                                    targetValue = if (isNextClicked) 400.dp else 0.dp,
                                    animationSpec = tween(600),
                                    label = "NextButtonExit"
                                )

                                Column(
                                    modifier = Modifier
                                        .align(BiasAlignment(animatedBias, 1f)),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (startFlicker && !isNextClicked) {
                                        Point(
                                            modifier = Modifier
                                        )
                                    }

                                    NextNumber(
                                        modifier = Modifier
                                            .padding(horizontal = 24.dp)
                                            .offset(x = nextButtonOffset),
                                        nextNumber = uiState.nextNumber.toString(),
                                        busDrawableRes = if (isLightIcon) R.drawable.ic_school_bus_light else R.drawable.ic_school_bus,
                                        onClick = {
                                            isNextClicked = true
                                            afterDraw()
                                        }
                                    )
                                }
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

                        PlayState.CHOOSE_NUMBER -> {
                            ChooseNumber(
                                Modifier.align(Alignment.Center),
                                maxAvailableNumber = uiState.maxAvailableNumber,
                                onNumberClick = onNumberChosen
                            )
                        }
                    }
                }
            }

            SuccessLottie(
                isVisible = uiState.showSuccessLottie,
                modifier = Modifier
                    .fillMaxSize()
            )

            DogAnimate(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                isVisible = uiState.showErrorLottie
            )
        }


        TimeTicker(
            modifier = Modifier
                .align(if (isHorizontal) Alignment.CenterEnd else Alignment.TopCenter)
                .padding(32.dp),
            state = tickerState
        )

        Column(
            modifier = Modifier.align(if (isHorizontal) Alignment.BottomStart else Alignment.BottomCenter),
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
