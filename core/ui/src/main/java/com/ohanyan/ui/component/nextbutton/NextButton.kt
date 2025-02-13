package com.ohanyan.ui.component.nextbutton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val padding by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse,
        ), label = ""
    )

    AnimatedVisibility(
        modifier = modifier.padding(bottom = (8 * padding).dp),
        visible = visible,
    ) {
        Image(
            modifier = Modifier
                .padding(32.dp),
            painter = painterResource(R.drawable.ic_next),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}