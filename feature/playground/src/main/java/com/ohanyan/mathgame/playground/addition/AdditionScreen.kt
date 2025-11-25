package com.ohanyan.mathgame.playground.addition

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.countpicker.CountPicker
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun AdditionScreen(
    viewModel: AdditionViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle one-time effects
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is AdditionEffect.ShowCorrectFeedback -> {
                    // You can add haptic feedback, sound, or other one-time actions here
                    // For now, the state change handles the visual feedback
                }
                is AdditionEffect.ShowWrongFeedback -> {
                    // You can add vibration or sound here
                }
            }
        }
    }

    AdditionScreenUI(
        uiState = uiState,
        onBackClick = onBackClick,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun AdditionScreenUI(
    uiState: AdditionUIState,
    onIntent: (AdditionIntent) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // Animate feedback scale
    val feedbackScale by animateFloatAsState(
        targetValue = when (uiState.isAnswerCorrect) {
            true -> 1f
            false -> 0.95f
            null -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "feedback_scale"
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
            modifier = Modifier.align(Alignment.Center),
            title = "Solve the problem"
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(vertical = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Display the math problem
                Text(
                    text = "${uiState.firstNumber} + ${uiState.secondNumber} = ?",
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.secondaryWhite,
                    fontSize = 48.sp,
                )

                // Generate answer options
                val correctAnswer = uiState.firstNumber + uiState.secondNumber
                val wrongAnswer1 = correctAnswer + 1
                val wrongAnswer2 = correctAnswer - 1
                val options = listOf(correctAnswer, wrongAnswer1, wrongAnswer2).shuffled()

                Row(
                    modifier = Modifier
                        .padding(top = 48.dp)
                        .scale(feedbackScale),
                    horizontalArrangement = Arrangement.spacedBy(36.dp)
                ) {
                    options.forEach { option ->
                        CountPicker(
                            text = option.toString(),
                            circleColor = MathAppTheme.colors.mainBlue,
                            onClick = {
                                onIntent(AdditionIntent.AnswerSelected(option.toString()))
                            }
                        )
                    }
                }

                // Feedback
                if (uiState.isAnswerCorrect == true) {
                    val checkmarkScale by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "checkmark_scale"
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .scale(checkmarkScale),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = MathAppTheme.colors.coreGreen,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                color = MathAppTheme.colors.coreWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Correct!",
                            style = MathAppTheme.typography.chalk,
                            color = MathAppTheme.colors.coreGreen,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (uiState.isAnswerCorrect == false) {
                    Text(
                        text = "Try again!",
                        style = MathAppTheme.typography.chalk,
                        color = MathAppTheme.colors.red,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
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
fun AdditionScreenPreview() {
    AdditionScreenUI(
        uiState = AdditionUIState(
            firstNumber = 3,
            secondNumber = 5,
            userAnswer = "",
            isAnswerCorrect = null
        ),
        onIntent = {}
    )
}
