package com.ohanyan.ui.component.agepickitem

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlin.random.Random

@Composable
fun SelectAgeItem(
    text: String,
    modifier: Modifier = Modifier,
    circleColor: Color = MathAppTheme.colors.coreYellow,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val textColor: Color = MathAppTheme.colors.coreYellow

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(500, 900)),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(Random.nextInt(0, 900))
        ), label = ""
    )
    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(top = (32 * scale).dp)
    ) {
        Text(
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = circleColor,
                        radius = size.minDimension / 2,
                        style = if (selected) Fill else Stroke(width = 4.dp.toPx()),
                    )

                }
                .padding(32.dp),
            text = text,
            style = MathAppTheme.typography.display,
            color = if (selected) MathAppTheme.colors.coreWhite else circleColor
        )
    }
}

@Preview
@Composable
fun AgePickItemPreview() {
    SelectAgeItem(
        text = "1"
    )
}


