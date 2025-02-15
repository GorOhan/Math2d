package com.ohanyan.ui.component.corebutton

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun CoreButton(
    buttonTitle: String = "Button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

            }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.mainBlue.copy(1f),
                        MathAppTheme.colors.mainBlue.copy(0.8f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                border = BorderStroke(
                    2.dp, color = MathAppTheme.colors.blue30,
                ),
                shape = RoundedCornerShape(16.dp)
            )

    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            text = buttonTitle,
            style = MathAppTheme.typography.h2,
            color = MathAppTheme.colors.coreWhite
        )

    }
}

@Preview
@Composable
fun CoreButtonPreview() {
    CoreButton()
}