package com.ohanyan.mathgame.onboarding.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.component.greetingmessage.GreetingMessage
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mathaction.MathLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onAnimationEnd: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SplashScreenUI(
        uiState = uiState,
        onAnimationEnd = onAnimationEnd,
    )
}

@Composable
fun SplashScreenUI(
    uiState: SplashUIState,
    onAnimationEnd: () -> Unit = {},
) {


    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }
    var offsetXGreeting by remember { mutableStateOf(0.dp) }

    var showGreetingMessage by remember { mutableStateOf(false) }
    var animateGreetingMessage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffsetX by animateDpAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 2000), label = "" // Animation duration for each step
    )
    val animatedOffsetXGreeting by animateDpAsState(
        targetValue = offsetXGreeting,
        animationSpec = tween(durationMillis = 1200), label = "" // Animation duration for each step
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = tween(durationMillis = 2000),
        finishedListener = {
            showGreetingMessage = true
            coroutineScope.launch {
                delay(500L)
                offsetX += 600.dp
                offsetXGreeting += 240.dp
                delay(1200)
                animateGreetingMessage = true
                delay(1500L)
                onAnimationEnd()

            }
        },
        label = ""
    )



    LaunchedEffect(Unit) {
        coroutineScope.launch {
            repeat(1) { step ->
                offsetY -= 90.dp
                delay(1400)
            }
        }
    }

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

        MathLoading(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            iconSize = 46.dp
        )

        if (showGreetingMessage) {
            GreetingMessage(
                modifier = Modifier
                    .padding(start = 42.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = animatedOffsetXGreeting),
                withAnimation = animateGreetingMessage
            )
        }

        MainHero(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .offset(
                    x = animatedOffsetX,
                    y = animatedOffsetY
                )
        )
    }
}

@Preview()
@Composable
fun SplashPreview() {
    SplashScreen()
}

