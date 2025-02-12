package com.ohanyan.mathgame.onboarding.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
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
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.onboarding.R
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

    var appleY by remember { mutableStateOf(0.dp) }
    var appleX by remember { mutableStateOf(0.dp) }
    val rotation = remember { Animatable(0f) }
    var currentRotation by remember { mutableStateOf(0f) }

    val appleAnimateState by animateDpAsState(
        targetValue = appleY,
        animationSpec = tween(durationMillis = 900),
        finishedListener = {
            coroutineScope.launch {
//                rotation.animateTo(
//                    targetValue = currentRotation - 360f,
//                    animationSpec = infiniteRepeatable(
//                        animation = tween(700, easing = LinearEasing),
//                        repeatMode = RepeatMode.Restart
//                    )
//                )
//                currentRotation = rotation.value
            }
//            appleX -= 300.dp
        },
        label = "" // Animation duration for each step
    )
    val appleAnimateStateX by animateDpAsState(
        targetValue = appleX,
        animationSpec = tween(durationMillis = 2500),
        finishedListener = {
            coroutineScope.launch {
                rotation.stop()
            }
        },
        label = "" // Animation duration for each step
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
                onAnimationEnd()
              //  appleY += 300.dp

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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.mainBlue.copy(0.7f),
                        MathAppTheme.colors.mainBlue.copy(0.2f)
                    )
                )
            )
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = appleAnimateStateX,
                    y = appleAnimateState
                )
                .rotate(rotation.value)
                .padding(32.dp),
            painter = painterResource(R.drawable.ic_apple),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 48.dp, top = 100.dp)
                .rotate(80F),

            painter = painterResource(R.drawable.ic_set_square),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
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

        AnimatedPreloader(
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

@Preview()
@Composable
fun SplashPreview() {
    SplashScreen()
}

