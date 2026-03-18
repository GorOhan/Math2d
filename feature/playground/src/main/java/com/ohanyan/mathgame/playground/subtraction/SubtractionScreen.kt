package com.ohanyan.mathgame.playground.subtraction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mainhero.DogAnimate
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mainhero.Touch
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType
import com.ohanyan.ui.component.soundbutton.SoundButton

@Composable
internal fun SubtractionScreen(
    viewModel: SubtractionViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SubtractionEffect.ShowCorrectFeedback -> {}
                is SubtractionEffect.ShowWrongFeedback -> {}
            }
        }
    }

    SubtractionScreenUI(
        uiState = uiState,
        onBackClick = onBackClick,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun SubtractionScreenUI(
    uiState: SubtractionUIState,
    onIntent: (SubtractionIntent) -> Unit = {},
    onBackClick: () -> Unit = {}
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

        SoundButton(
            modifier = Modifier.align(Alignment.TopEnd),
            isOn = uiState.isMusicOn,
            onClick = { onIntent(SubtractionIntent.ToggleMusic(it)) }
        )

        ChalkBoard(
            modifier = Modifier.align(Alignment.Center),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "${uiState.firstNumber} - ${uiState.secondNumber} = ?",
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.secondaryWhite,
                    fontSize = 112.sp,
                )

                SubtractionImagesGrid(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    firstNumber = uiState.firstNumber,
                    secondNumber = uiState.secondNumber,
                    imageResId = uiState.imgResId,
                    key = "${uiState.firstNumber}-${uiState.secondNumber}",
                )

                SubtractionSelectNumbers(
                    uiState = uiState,
                    onIntent = onIntent,
                )
            }

            DogAnimate(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                isVisible = uiState.isAnswerCorrect == false
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
fun SubtractionScreenPreview() {
    SubtractionScreenUI(
        uiState = SubtractionUIState(
            firstNumber = 7,
            secondNumber = 3,
            userAnswer = "",
            isAnswerCorrect = null,
            isMusicOn = true,
            options = listOf(4, 2, 6),
        ),
        onIntent = {}
    )
}

@Composable
private fun SubtractionImagesGrid(
    modifier: Modifier,
    firstNumber: Int,
    secondNumber: Int,
    imageResId: Int,
    key: String,
) {
    if (firstNumber > 15) return
    key(key) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(firstNumber - secondNumber) { index ->
                    FallingSubtractionImage(
                        imageResId = imageResId,
                        index = index,
                        key = key,
                    )
                }
            }
        }
}

@Composable
private fun FallingSubtractionImage(
    imageResId: Int,
    index: Int,
    key: String,
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key) {
        kotlinx.coroutines.delay((index * 100).toLong())
        startAnimation = true
    }

    val offsetY by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "sub_falling_animation_$index"
    )

    Image(
        painter = painterResource(imageResId),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .alpha(offsetY)
            .padding(horizontal = 4.dp)
    )
}

@Composable
fun SubtractionSelectNumbers(
    uiState: SubtractionUIState,
    onIntent: (SubtractionIntent) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(88.dp)
    ) {
        uiState.options.forEach { option ->
            val isSelected = uiState.selectedAnswer == option
            val isCorrect = uiState.isAnswerCorrect == true
            val isWrong = uiState.isAnswerCorrect == false

            val circleColor = when {
                isSelected && isCorrect -> MathAppTheme.colors.secondaryWhite
                isSelected && isWrong -> MathAppTheme.colors.secondaryWhite
                isSelected -> Color.White
                else -> Color.Transparent
            }

            val angle by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = LinearOutSlowInEasing
                ),
                label = ""
            )
            Box(
                modifier = Modifier.size(104.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp)
                ) {
                    if (isSelected && isCorrect) {
                        drawArc(
                            color = circleColor,
                            -45f,
                            360f * angle,
                            useCenter = false,
                            style = Stroke(4.dp.toPx(), cap = StrokeCap.Butt)
                        )
                    } else if (isSelected && isWrong) {
                        val strokeWidth = 4.dp.toPx()
                        val padding = 15.dp.toPx()

                        val line1Progress = (angle * 2).coerceIn(0f, 1f)
                        val line2Progress = ((angle - 0.5f) * 2).coerceIn(0f, 1f)

                        if (line1Progress > 0f) {
                            drawLine(
                                color = circleColor,
                                start = Offset(padding, padding),
                                end = Offset(
                                    padding + (size.width - 2 * padding) * line1Progress,
                                    padding + (size.height - 2 * padding) * line1Progress
                                ),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }

                        if (line2Progress > 0f) {
                            drawLine(
                                color = circleColor,
                                start = Offset(size.width - padding, padding),
                                end = Offset(
                                    size.width - padding - (size.width - 2 * padding) * line2Progress,
                                    padding + (size.height - 2 * padding) * line2Progress
                                ),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
                Text(
                    text = option.toString(),
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.secondaryWhite,
                    fontSize = 62.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onIntent(SubtractionIntent.AnswerSelected(option))
                        }
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val white = androidx.compose.ui.graphics.Color.White
                            drawLine(
                                color = white,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height * 3 / 4),
                                strokeWidth = strokeWidth
                            )
                        }
                )

                this@Row.AnimatedVisibility(
                    visible = uiState.hintOption == option,
                    enter = fadeIn()
                ) {
                    Touch(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(start = 64.dp, bottom = 32.dp),
                    )
                }
            }
        }
    }
}
