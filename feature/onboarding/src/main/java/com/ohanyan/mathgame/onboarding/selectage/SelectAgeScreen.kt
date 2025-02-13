package com.ohanyan.mathgame.onboarding.selectage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.mathgame.onboarding.splash.AnimatedPreloader
import com.ohanyan.ui.component.TypingAnimation
import com.ohanyan.ui.component.agepickitem.SelectAgeItem

@Composable
fun SelectAgeScreen(
    viewModel: SelectAgeViewModel = hiltViewModel(),
    onNextClick: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsState()

    SelectAgeScreenUI(
        chooseAgeUIState = uiState,
        onSelectAge = viewModel::selectAge,
        onNextClick = onNextClick,
    )
}

@Composable
fun SelectAgeScreenUI(
    chooseAgeUIState: ChooseAgeUIState,
    onSelectAge: (String) -> Unit = {},
    onNextClick: () -> Unit = {},
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
            )
    ) {

        ActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = chooseAgeUIState.selectedAge.isNotEmpty(),
            onClick = onNextClick
        )

        Column {
            Row(
                modifier = Modifier.padding(top = 24.dp, start = 124.dp)
            ) {
                AnimatedPreloader(modifier = Modifier.size(112.dp))

                TypingAnimation("Select your child age !")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                chooseAgeUIState.ageOptions.forEach { age ->
                    SelectAgeItem(
                        text = age.age,
                        modifier = Modifier.padding(top = age.topPadding.dp),
                        circleColor = getColorForAge(age.color),
                        selected = chooseAgeUIState.selectedAge == age.age,
                        withAnimation = chooseAgeUIState.selectedAge.isEmpty(),
                        onClick = { onSelectAge(age.age) }
                    )
                }
            }

        }
    }
}

@Composable
fun getColorForAge(ageColor: AgeColor) =
    when (ageColor) {
        AgeColor.RED -> MathAppTheme.colors.red
        AgeColor.BLUE -> MathAppTheme.colors.mainBlue
        AgeColor.GREEN -> MathAppTheme.colors.coreGreen
        AgeColor.YELLOW -> MathAppTheme.colors.coreYellow
    }
