package com.ohanyan.ui.component.numberhint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ohanyan.mathgame.ui.R

@Composable
fun NumberHint(
    modifier: Modifier = Modifier,
    number: Int = 0,
) {

    val res = when (number) {
        0 -> R.raw.zero
        1 -> R.raw.one
        2 -> R.raw.two
        3 -> R.raw.three
        4 -> R.raw.four
        5 -> R.raw.five
        6 -> R.raw.six
        7 -> R.raw.seven
        8 -> R.raw.eigh
        9 -> R.raw.nine
        else -> R.raw.three
    }
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(res)
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
            .fillMaxSize()
    )
}