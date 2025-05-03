package com.ohanyan.mathgame.playground.counting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.playground.R
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.countpicker.CountPicker
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mainhero.MathConfetti
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
        onOptionSelected = viewModel::onOptionSelected
    )

}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LearnCountScreenUI(
    uiState: LearnCountUIState,
    onOptionSelected: (String) -> Unit = {},
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
            modifier = Modifier.align(Alignment.Center),
            title = stringResource(id = R.string.feature_playground_how_many_items)
        ) {

            val currentTest = uiState.currentTest
            FlowRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(vertical = 64.dp),
                horizontalArrangement = Arrangement.Center,

                ) {
                repeat(uiState.currentTest?.count ?: 0) {
                    Image(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(64.dp),
                        painter = painterResource(
                            currentTest?.imgResId ?: com.ohanyan.mathgame.ui.R.drawable.ic_balloons
                        ),
                        contentDescription = null
                    )
                }

            }


            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(36.dp)
            ) {
                uiState.currentTest?.answerOptions?.forEach {
                    CountPicker(
                        text = it,
                        circleColor = MathAppTheme.colors.mainBlue,
                        onClick = {
                            onOptionSelected(it)
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (uiState.isAnsweredCorrect ?: false) {
                MathConfetti(
                    modifier = Modifier.size(144.dp)
                )
            }

            MainHero(
                modifier = Modifier
                    .padding(16.dp)
                    .size(112.dp)
            )
        }
    }
}

@Preview
@Composable
fun LearnCountScreenPreview() {
    LearnCountScreenUI(LearnCountUIState())
}
