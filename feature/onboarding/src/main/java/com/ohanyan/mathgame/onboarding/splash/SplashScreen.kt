package com.ohanyan.mathgame.onboarding.splash

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SplashScreenUI(uiState = uiState)
}

@Composable
fun SplashScreenUI(uiState: SplashUIState) {

    val transition = rememberInfiniteTransition()
    val heartbeatAnimation by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

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

    var appleY by remember { mutableStateOf(0.dp) }
    var appleX by remember { mutableStateOf(0.dp) }
    val rotation = remember { Animatable(0f) }
    var currentRotation by remember { mutableStateOf(0f) }

    val appleAnimateState by animateDpAsState(
        targetValue = appleY,
        animationSpec = tween(durationMillis = 900),
        finishedListener = {
            coroutineScope.launch {
                rotation.animateTo(
                    targetValue = currentRotation - 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(700, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                currentRotation = rotation.value
            }
            appleX -= 300.dp
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
                offsetXText += 240.dp
                delay(1200)
                animateGreetingMessage = true
                appleY += 300.dp

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
            .background(color = MathAppTheme.colors.mainBlue)
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
//
//        Image(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .padding(start = 32.dp,bottom = 164.dp),
//            painter = painterResource(R.drawable.giraff),
//            contentScale = ContentScale.FillWidth,
//            contentDescription = null,
//        )
//
//        Image(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .padding(start = 104.dp, bottom = 48.dp),
//            painter = painterResource(R.drawable.ic_balloons),
//            contentScale = ContentScale.FillWidth,
//            contentDescription = null,
//        )
//
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 48.dp, top = 100.dp)
                .rotate(80F)
            ,

            painter = painterResource(R.drawable.ic_set_square),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        Image(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 48.dp, top = 100.dp)
                .rotate(20F)
            ,

            painter = painterResource(R.drawable.ic_set_square),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        if (showGreetingMessage) {
            //    TextLikeSpeakEffect("Hello Gor")
            GreetingMessage(
                modifier = Modifier
                    .padding(start = 42.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = animatedOffsetXForText),
                withAnimation = animateGreetingMessage
            )
        }

//        val ids = listOf(R.drawable.giraff,R.drawable.fox,R.drawable.chipmunk)
//        Row(
//            modifier = Modifier .align(Alignment.Center)
//        ){
//            ids.forEach {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.FillWidth,
//                    modifier = Modifier
//                        .size(150.dp)
//                        .padding(horizontal = 16.dp)
//                        .scale(heartbeatAnimation),
//                        //  .alpha(flashAnimation)
//                        //  .rotate(wiggleAnimation),
//                    contentDescription = null,
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

@Preview()
@Composable
fun SplashPreview() {
    SplashScreen()
}

@Composable
fun TextLikeSpeakEffect(text: String) {
    // Animating the size of the text to simulate speaking
    val infiniteTransition = rememberInfiniteTransition()
    val animatedFontSize by infiniteTransition.animateFloat(
        initialValue = 16f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = text,
            fontSize = animatedFontSize.sp,
            fontWeight = FontWeight.Bold,
            color = animatedColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun TextLikeSpeakScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        TextLikeSpeakEffect("Hello, I'm speaking!")
    }
}