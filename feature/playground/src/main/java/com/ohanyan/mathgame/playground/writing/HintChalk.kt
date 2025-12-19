package com.ohanyan.mathgame.playground.writing

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R

@Composable
fun HintChalk(
    modifier: Modifier = Modifier,
  ) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun returnPadding(): Float {
        val padding by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse,
            ), label = ""
        )
        return padding
    }

    val padding = returnPadding()

    Box(
        modifier = modifier
            .padding(
                PaddingValues(bottom = (14 * padding).dp)),
    ) {
        Image(
            modifier = Modifier.padding(32.dp),
            painter = painterResource(R.drawable.ic_chalk),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}