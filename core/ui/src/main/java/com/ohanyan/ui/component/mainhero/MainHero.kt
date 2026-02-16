package com.ohanyan.ui.component.mainhero

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ohanyan.mathgame.ui.R
import kotlinx.coroutines.delay


@Composable
fun MainHero(
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.happy_dog
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
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

@Composable
fun MathConfetti(
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.math_conf
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = { preloaderProgress },
        modifier = modifier
    )
}

@Composable
fun EraseEffect(
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.fastclean,
    onFinish: () -> Unit = {},
    onUndo: () -> Unit = {}
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = true
    )

    LaunchedEffect(preloaderProgress) {
        if (preloaderProgress == 1f) {
            onFinish()
        }
        if (preloaderProgress >= .6f) {
            onUndo()
        }
    }

    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = {
            preloaderProgress
        },
        modifier = modifier
    )
}

@Composable
fun DogAnimate(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    @RawRes lottieRes: Int = R.raw.dog,
    onFinish: () -> Unit = {},
    onUndo: () -> Unit = {}
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = isVisible
    )


    LaunchedEffect(preloaderProgress) {
        if (preloaderProgress == 1f) {
            onFinish()
        }
        if (preloaderProgress >= .6f) {
            onUndo()
        }
    }


    if (isVisible) {
        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = {
                preloaderProgress
            },
            modifier = modifier.size(124.dp),
        )
    }
}

@Composable
fun Touch(
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.touch,
    onFinish: () -> Unit = {},
) {
    var isFinished by remember { mutableStateOf(false) }
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 3,
        isPlaying = true
    )


    LaunchedEffect(preloaderProgress) {
        if (preloaderProgress == 1f) {
            onFinish()
            delay(500L)
            isFinished = true
        }
    }

    if (!isFinished) {
        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = {
                preloaderProgress
            },
            modifier = modifier
                .rotate(240f)
                .size(64.dp),
        )
    }
}