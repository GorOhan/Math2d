package com.ohanyan.mathgame.onboarding.menu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.corebutton.CoreButton
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mainhero.MathConfetti
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onLearnNumber: () -> Unit = {},
    onCountClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    MenuScreenUI(
        onLearnNumber = onLearnNumber,
        onBackClick = onBackClick,
        onCountClick = onCountClick
    )
}

@Composable
fun MenuScreenUI(
    onLearnNumber: () -> Unit = {},
    onCountClick: () -> Unit = {},
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
            )
    ) {
        ActionButton(
            modifier = Modifier.align(Alignment.TopStart),
            visible = true,
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

        MainHero(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.TopCenter)
                .size(112.dp)
        )

        var offsetXSpace by remember { mutableStateOf(0.dp) }

        val animateOffsetX by animateDpAsState(
            targetValue = offsetXSpace,
            animationSpec = tween(durationMillis = 6000), label = ""
        )
        LaunchedEffect(Unit) {
            offsetXSpace += 1200.dp
        }
//        MathConfetti(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .size(160.dp)
//                .offset(x = animateOffsetX)
//        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoreButton(
                buttonTitle = "Count",
                onClick = onCountClick
            )

            CoreButton(
                buttonTitle = "Learn Number",
                onClick = onLearnNumber
            )

            MathLoading(
                durationOfEachAnimation = 200
            )
        }
    }
}

@Composable
@Preview
fun MenuScreenPreview() {
    MenuScreenUI()
}