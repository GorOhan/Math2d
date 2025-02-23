package com.ohanyan.ui.component.success

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ohanyan.mathgame.ui.R

@Composable
fun SuccessLottie(
    isVisible: Boolean = false,
    modifier: Modifier = Modifier,
    @RawRes lottieRes: Int = R.raw.success
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = isVisible,
    )


    if (isVisible) {
        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = { preloaderProgress },
            modifier = modifier
        )
    }
}