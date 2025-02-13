package com.ohanyan.mathgame.onboarding.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.onboarding.splash.AnimatedPreloader
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    MenuScreenUI(
        onBackClick = onBackClick
    )
}

@Composable
fun MenuScreenUI(
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
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Menu",
                color = MathAppTheme.colors.mainBlue,
                style = MathAppTheme.typography.display
            )

            MathLoading()
        }


        ActionButton(
            modifier = Modifier.align(Alignment.TopStart),
            visible = true,
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

        AnimatedPreloader(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(112.dp)
        )
    }
}