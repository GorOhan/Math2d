package com.ohanyan.ui.component.mathaction

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.ui.R

@Composable
fun MathLoading(
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    durationOfEachAnimation: Int = 300
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun createScaleAnimation(startOffsetMultiplier: Int): Float {
        val scale by infiniteTransition.animateFloat(
            initialValue = .7f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationOfEachAnimation,
                    delayMillis = durationOfEachAnimation * 3
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(2 * durationOfEachAnimation * startOffsetMultiplier)
            ), label = ""
        )
        return scale
    }

    val scales = List(4) { createScaleAnimation(it) }

    val icons = listOf(
        R.drawable.ic_plus to MathAppTheme.colors.red,
        R.drawable.ic_minus to MathAppTheme.colors.coreGreen,
        R.drawable.ic_multiplication to MathAppTheme.colors.mainBlue,
        R.drawable.ic_division to MathAppTheme.colors.coreYellow
    )

    Row(modifier = modifier) {
        icons.forEachIndexed { index, (iconRes, tintColor) ->
            Icon(
                modifier = Modifier
                    .scale(scales[index])
                    .size(iconSize),
                painter = painterResource(iconRes),
                tint = tintColor,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun MathLPreview() {
    MathLoading()
}