package com.ohanyan.mathgame.onboarding.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ohanyan.mathgame.designsystem.component.greetingmessage.GreetingMessage
import com.ohanyan.mathgame.designsystem.component.header.HeaderFromTop
import com.ohanyan.mathgame.onboarding.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SplashScreenUI(uiState = uiState)
}

@Composable
fun SplashScreenUI(uiState: SplashUIState) {

    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetXText by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }

    var showGreetingMessage by remember { mutableStateOf(false) }
    var animateGreetingMessage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffsetX by animateDpAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 2000), label = "" // Animation duration for each step
    )
    val animatedOffsetXForText by animateDpAsState(
        targetValue = offsetXText,
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
                offsetXText += 240.dp
                delay(1200)
                animateGreetingMessage = true
            }
        },
        label = "" // Animation duration for each step
    )


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            repeat(1) { step ->
                offsetY -= 90.dp
                delay(1400) // Wait for the animation to complete
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.backtest2),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
        )
        if (showGreetingMessage) {
            GreetingMessage(
                modifier = Modifier
                    .padding(start = 42.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = animatedOffsetXForText),
                withAnimation = animateGreetingMessage
            )
        }

        AnimatedPreloader(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .offset(
                    x = animatedOffsetX,
                    y = animatedOffsetY
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        offsetX += dragAmount.x.toDp()
                        offsetY += dragAmount.y.toDp()
                    }
                }
        )
    }
}

@Composable
fun AnimatedPreloader(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.main_hero
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = { preloaderProgress },
        modifier = modifier
    )
}

@Preview
@Composable
fun SplashPreview() {
    SplashScreen()
}