package com.ohanyan.ui.component.countpicker

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlin.random.Random

@Composable
fun CountPicker(
    text: String,
    modifier: Modifier = Modifier,
    circleColor: Color = MathAppTheme.colors.red,
    withAnimation: Boolean = true,
    onClick: () -> Unit = {},
) {
    val borderColor: Color = MathAppTheme.colors.secondaryWhite

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = ""
    )

    val padding by if (withAnimation) {
        infiniteTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(Random.nextInt(500, 900)),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(Random.nextInt(0, 900))
            ), label = ""
        )
    } else {
        remember { mutableFloatStateOf(0.7f) }
    }
    Box(
        modifier = modifier
            .scale(scale)
            .padding(bottom = (12 * padding).dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        circleColor,
                        circleColor.copy(0.8f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                border = BorderStroke(
                    2.dp, color = borderColor,
                ),
                shape = RoundedCornerShape(16.dp)
            )

    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 64.dp, vertical = 8.dp),
            text = text,
            style = MathAppTheme.typography.h1Bee,
            color = MathAppTheme.colors.coreWhite
        )
    }
}

@Preview
@Composable
fun AgePickItemPreview() {
    CountPicker(
        text = "1"
    )
}


